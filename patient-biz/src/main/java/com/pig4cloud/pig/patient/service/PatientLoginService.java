package com.pig4cloud.pig.patient.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.pig.patient.request.BindPhoneNumberRequest;
import com.pig4cloud.pig.patient.utils.HTTPUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * <h3>patient</h3>
 *
 * @author HuangJiayu
 * @description <p>小程序登录服务类</p>
 * @date 2024-07-26 16:26
 **/
@Service
@Slf4j
public class PatientLoginService {
	
	@Value("${wxchat.appid}")
	private String appid;
	@Value("${wxchat.secret}")
	private String secret;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	private final RestTemplate restTemplate;
	
	public PatientLoginService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@Autowired
	private HTTPUtils httpUtils;
	
	@Autowired
	private PatientBaseService patientBaseService;
	
	// 自定义小程序登录流程
	public R customLogin(String openId) {
		// 根据用户的openId，查询数据库是否存在该用户
		LambdaQueryWrapper<PatientBaseEntity> wrapper = Wrappers.lambdaQuery();
		PatientBaseEntity patientBaseEntity = new PatientBaseEntity();
		patientBaseEntity.setWxUid(openId);
		PatientBaseEntity one = patientBaseService.getOne(wrapper);
		if (one != null && one.getPhoneNumber() != null && !one.getPhoneNumber().isBlank()) {
			//	手机号存在直接用该手机号登录
			return this.pigLogin(one);
		} else {
			// 手机号或者用户不存在则需要提醒用户绑定手机号，创建对应的pig账号，之后再次登录
			return R.failed("请绑定手机号");
		}
	}
	
	public R pigLogin(PatientBaseEntity one) {
		//	手机号存在直接用该手机号登录
		Map<String, Object> params = new HashMap<>();
		params.put("username", one.getPhoneNumber());
		params.put("password", one.getPhoneNumber());
		params.put("grant_type", "password");
		params.put("scope", "server");
		String url = "http://127.0.0.1:9999/auth/oauth2/token";
		try {
			JSONObject post = httpUtils.post(url, params,
			 MediaType.APPLICATION_FORM_URLENCODED);
			return R.ok(post);
		} catch (Exception e) {
			log.error("登录pig失败", e);
			return R.failed("登录pig失败");
		}
	}
	
	public R pigRegister(Map<String, Object> params) {
		String url = "http://127.0.0.1:9999/admin/custom_register/user";
		try {
			JSONObject post = httpUtils.post(url, params);
			return R.ok(post);
		} catch (Exception e) {
			log.error("注册pig失败", e);
			return R.failed("注册pig失败");
		}
		
	}
	
	// 绑定手机号并完成登录
	public R bindPhoneNumber(BindPhoneNumberRequest bindPhoneNumberRequest) {
		//	获取该用户的手机号码
		R phoneNumberR = this.getPhoneNumber(bindPhoneNumberRequest.getPhoneCode());
		if (phoneNumberR.getCode() != 0) {
			return phoneNumberR;
		}
		JSONObject phoneNumberJson = (JSONObject) phoneNumberR.getData();
		String phoneNumber = phoneNumberJson.getString("phoneNumber");
		// 根据openId查询用户，如果没有再创建新账户
		LambdaQueryWrapper<PatientBaseEntity> wrapper = Wrappers.lambdaQuery();
		PatientBaseEntity patientBaseEntity = new PatientBaseEntity();
		patientBaseEntity.setWxUid(bindPhoneNumberRequest.getOpenId());
		PatientBaseEntity one = patientBaseService.getOne(wrapper);
		PatientBaseEntity tmp = null;
		if (one == null) {
			//	创建新账户
			patientBaseEntity.setPhoneNumber(phoneNumber);
			patientBaseEntity.setUsername(phoneNumber);
			patientBaseService.save(patientBaseEntity);
			tmp = patientBaseEntity;
		} else {
			//	修改用户信息
			one.setPhoneNumber(phoneNumber);
			one.setUsername(phoneNumber);
			patientBaseService.updateById(patientBaseEntity);
			tmp = one;
		}
		//	创建pig用户并完成登录
		try {
			R r = this.pigLogin(tmp);
			if (r.getCode() == 0) {
				//	登录成功，已经有该账号，不需要再更改了
				return r;
			} else if (r.getMsg().contains("密码错误")) {
				//	没有该账号，自己创建该账号
				Map<String, Object> params = new HashMap<>();
				params.put("username", tmp.getPhoneNumber());
				params.put("password", tmp.getPhoneNumber());
				params.put("phone", tmp.getPhoneNumber());
				Long[] roles = {1811326313985011714L};
				params.put("role", roles);
				R r1 = this.pigRegister(params);
				if (r1.getCode() == 0) {
					//	直接登录获取token
					return this.pigLogin(tmp);
				} else {
					return r1;
				}
			} else {
				return R.failed("登录pig异常");
			}
		} catch (Exception e) {
			log.error("绑定手机号并完成登录失败", e);
			return R.failed("绑定手机号并完成登录失败");
		}
	}
	
	// TODO: 手机号换绑
	
	
	// 获取微信小程序的接口调用凭据
	public R getWXAccessToken() {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appid}&secret={secret}";
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("appid", this.appid);
		requestMap.put("secret", this.secret);
		try {
			ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class,
			 requestMap);
			
			JSONObject jsonObject = JSONObject.parseObject(
			 Objects.requireNonNull(forEntity.getBody()).toString());
			// 校验是否有错误
			if (jsonObject.containsKey("errcode")) {
				R.failed(jsonObject.getString("errmsg"));
			}
			return R.ok(jsonObject);
		} catch (Exception e) {
			log.error("获取微信小程序调用凭证错误", e);
			return R.failed("获取微信小程序调用凭证错误");
		}
	}
	
	// 存储微信小程序调用的Token
	public R saveWXAccessToken() {
		// 先检查redis中是否存在微信token，不存在则重新获取，设置token时间为6000秒，小于微信小程序原生token时间
		R r = this.existWXAccessToken();
		if (r.getCode() == 1) {
			//	不存在，那么重新获取
			R wxAccessToken = this.getWXAccessToken();
			if (wxAccessToken.getCode() == 0) {
				// 调用成功
				JSONObject wxAccessTokenJson =
				 (JSONObject) JSONObject.toJSON(wxAccessToken.getData());
				String wxAccessTokenString = wxAccessTokenJson.getString("access_token");
				try {
					redisTemplate.boundValueOps("wx_access_token").set(wxAccessTokenString, 6000,
					 TimeUnit.SECONDS);
					// note: 返回的data 是一个String类型，可以直接使用
					return R.ok(wxAccessTokenString);
				} catch (Exception e) {
					return R.failed("将微信token存入redis失败");
				}
			} else {
				// 调用失败
				return wxAccessToken;
			}
		} else {
			//	存在，那么直接返回
			return r;
		}
	}
	
	public R existWXAccessToken() {
		String wxAccessToken = (String) redisTemplate.boundValueOps("wx_access_token").get();
		if (wxAccessToken != null) {
			return R.ok(wxAccessToken);
		}
		return R.failed();
	}
	
	
	public R getPhoneNumber(String code) {
		// 获取wx_access_token
		R r = this.saveWXAccessToken();
		if (r.getCode() == 1) {
			return r;
		}
		String wxAccessToken = (String) r.getData();
		String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token="
		 + wxAccessToken;
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("code", code);
		JSONObject jsonObject = httpUtils.post(url, requestMap);
		// 检测是否有错误
		if (jsonObject.containsKey("errcode") && jsonObject.getInteger("errcode") != 0) {
			return R.failed(jsonObject.getString("errmsg"));
		}
		return R.ok(jsonObject.getJSONObject("phone_info"));
	}
	
	public R getWxOpenId(String code) {
		//	获取该用户的openId
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("appid", this.appid);
		requestMap.put("secret", this.secret);
		requestMap.put("js_code", code);
		try {
			ResponseEntity forEntity = restTemplate.getForEntity(
			 "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code"
			  + "={js_code}&grant_type=authorization_code",
			 String.class, requestMap);
			JSONObject jsonObject = JSONObject.parseObject(
			 Objects.requireNonNull(forEntity.getBody()).toString());
			// 校验是否有错误
			if (jsonObject.containsKey("errcode") && jsonObject.getInteger("errcode") != 0) {
				return R.failed(jsonObject.getString("errmsg"));
			}
			return R.ok(jsonObject);
		} catch (Exception e) {
			log.error("转发网络请求错误", e);
			return R.failed("转发网络请求错误");
		}
	}
}
