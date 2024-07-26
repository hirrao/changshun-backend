package com.pig4cloud.pig.patient.controller;

import com.alibaba.fastjson.JSONObject;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.pig.patient.request.BindPhoneNumberRequest;
import com.pig4cloud.pig.patient.service.PatientLoginService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * <h3>patient</h3>
 *
 * @author HuangJiayu
 * @description <p>患者登录</p>
 * @date 2024-07-26 10:37
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/patientLogin")
@Tag(description = "patientLogin", name = "患者用户登录管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
@Slf4j
public class PatientLoginController {
	
	
	private final RestTemplate restTemplate;
	
	@Autowired
	private PatientLoginService patientLoginService;
	
	@Inner(value = false)
	@GetMapping("/get_wx_open_id/{code}")
	public R getWxOpenId(@PathVariable String code) {
		//	获取微信的openId
		R r = patientLoginService.getWxOpenId(code);
		return r;
	}
	
	@Inner(value = false)
	@PostMapping("/custom_login")
	public R customLogin(@RequestBody JSONObject jsonObject) {
		String openId = jsonObject.getString("openId");
		//	自定义的登录服务
		R r = patientLoginService.customLogin(openId);
		return r;
	}
	
	@Inner(value = false)
	@PostMapping("/bind_phone_number")
	public R bindPhoneNumber(@RequestBody BindPhoneNumberRequest bindPhoneNumberRequest) {
		//	绑定手机号码
		R r = patientLoginService.bindPhoneNumber(bindPhoneNumberRequest);
		return r;	
	}
	
	
	@Inner(value = false)
	@PostMapping("/normal_test")
	public R normal_test(@RequestBody JSONObject jsonObject) {
		String code = jsonObject.getString("code");
		//	通用的测试service服务的接口
		R r = patientLoginService.getWxOpenId(code);
		return r;
	}
}



