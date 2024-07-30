package com.pig4cloud.pig.patient.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.pig.patient.request.BindPhoneNumberRequest;
import com.pig4cloud.pig.patient.utils.HTTPUtils;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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
	@Value("${wxchat.enc_key}")
	private String encKey;
	
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
		wrapper.setEntity(patientBaseEntity);
		// last("limit 1")避免存在多条数据时报错
		PatientBaseEntity one = patientBaseService.getOne(wrapper.last("limit 1"));
		if (one != null && one.getPhoneNumber() != null && !one.getPhoneNumber().isBlank()) {
			//	手机号存在直接用该手机号登录
			return this.pigLogin(one);
		} else {
			// 手机号或者用户不存在则需要提醒用户绑定手机号，创建对应的pig账号，之后再次登录
			return R.failed("请绑定手机号");
		}
	}
	
	public R pigLogin(PatientBaseEntity one) {
		// 缺少封装auth headers等，需要参考pig前端ui操作 手机号存在直接用该手机号登录
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth("dGVzdDp0ZXN0");
		// note: form表单提交只能用MultiValueMap
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("username", one.getPhoneNumber());
		//  密码需要加密
		try {
			params.add("password", encrypt(encKey, one.getPhoneNumber()));
		} catch (Exception e) {
			log.error("加密密码失败", e);
			return R.failed("加密密码失败");
		}
		String url = "http://pig-gateway:9999/auth/oauth2/token?grant_type=password&scope=server";
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
		try {
			ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, request,
			 JSONObject.class);
			if (response.getStatusCode() != HttpStatus.OK) {
				return R.failed("网络请求出现错误");
			}
			JSONObject post = response.getBody();
			return R.ok(post);
		} catch (HttpClientErrorException.Unauthorized e) {
			//	用户密码错误
			return R.failed("用户名或密码错误");
		} catch (HttpServerErrorException.InternalServerError e) {
			//	系统内部异常，请等待一段时间后再次访问
			return R.failed("系统内部异常，请等待一段时间后再次访问");
		} catch (Exception e) {
			log.error("登录pig失败", e);
			return R.failed("登录pig失败");
		}
	}
	
	// 采用AES对称加密，返回16字节的字符串,customKey 为密钥
	public static String encrypt(String customKey, String word) throws Exception {
		String algorithm = "AES";
		String transformation = "AES/CFB/NoPadding";
		SecretKeySpec secretKey = new SecretKeySpec(customKey.getBytes(StandardCharsets.UTF_8),
		 algorithm);
		IvParameterSpec ivSpec = new IvParameterSpec(customKey.getBytes(StandardCharsets.UTF_8));
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
		byte[] encrypt = cipher.doFinal(word.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(encrypt);
		
	}
	
	public R pigRegister(Map<String, Object> params) {
		String url = "http://pig-gateway:9999/admin/custom_register/user";
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
		//	获取该用户的手机号码，将phoneCode作为手机号码
		String phoneNumber = bindPhoneNumberRequest.getPhoneCode();
		// 根据openId查询用户，如果没有再创建新账户
		LambdaQueryWrapper<PatientBaseEntity> wrapper = Wrappers.lambdaQuery();
		PatientBaseEntity patientBaseEntity = new PatientBaseEntity();
		patientBaseEntity.setWxUid(bindPhoneNumberRequest.getOpenId());
		wrapper.setEntity(patientBaseEntity);
		PatientBaseEntity one = patientBaseService.getOne(wrapper.last("limit 1"));
		PatientBaseEntity tmp = null;
		System.out.println("查询条件的用户基本信息" + patientBaseEntity);
		if (one == null) {
			//	创建新账户
			patientBaseEntity.setPhoneNumber(phoneNumber);
			patientBaseEntity.setUsername(phoneNumber);
			boolean save = patientBaseService.save(patientBaseEntity);
			if (save) {
				tmp = patientBaseEntity;
			} else {
				return R.failed("创建新账户失败");
			}
		} else {
			System.out.println("查询得到的用户基本信息:" + one);
			//	修改用户信息
			one.setPhoneNumber(phoneNumber);
			one.setUsername(phoneNumber);
			//	由于可能对原有数据并没有任何更改，因此不能通过是否有修改来判断
			patientBaseService.updateById(one);
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
				return r;
			}
		} catch (Exception e) {
			log.error("绑定手机号并完成登录失败", e);
			return R.failed("绑定手机号并完成登录失败");
		}
	}
	
	// 手机号换绑
	public R changeBindPhoneNumber(BindPhoneNumberRequest bindPhoneNumberRequest) {
		//	根据openId查询对应的患者
		LambdaQueryWrapper<PatientBaseEntity> wrapper = Wrappers.lambdaQuery();
		PatientBaseEntity patientBaseEntity = new PatientBaseEntity();
		patientBaseEntity.setWxUid(bindPhoneNumberRequest.getOpenId());
		wrapper.setEntity(patientBaseEntity);
		PatientBaseEntity one = patientBaseService.getOne(wrapper.last("limit 1"));
		if (one == null) {
			return R.failed("查询患者失败");
		}
		return this.bindPhoneNumber(bindPhoneNumberRequest);
	}
	
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
				JSONObject wxAccessTokenJson = (JSONObject) JSONObject.toJSON(
				 wxAccessToken.getData());
				String wxAccessTokenString = wxAccessTokenJson.getString("access_token");
				try {
					redisTemplate.boundValueOps("wx_access_token")
					 .set(wxAccessTokenString, 6000, TimeUnit.SECONDS);
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
		String url =
		 "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + wxAccessToken;
		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("code", code);
		JSONObject jsonObject = httpUtils.post(url, requestMap);
		// 检测是否有错误
		if (jsonObject.containsKey("errcode") && jsonObject.getInteger("errcode") != 0) {
			return R.failed(jsonObject.getString("errmsg"));
		}
		return R.ok(jsonObject.getJSONObject("phone_info").getString("phoneNumber"));
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
			  + "={js_code}&grant_type=authorization_code", String.class, requestMap);
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
