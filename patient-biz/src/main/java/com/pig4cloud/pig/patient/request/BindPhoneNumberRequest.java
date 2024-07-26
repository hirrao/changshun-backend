package com.pig4cloud.pig.patient.request;

import lombok.Data;

/**
 * <h3>patient</h3>
 *
 * @author HuangJiayu
 * @description <p>绑定手机号并完成登录</p>
 * @date 2024-07-26 18:29
 **/
@Data
public class BindPhoneNumberRequest {
	
	private String openId;
	private String phoneCode;
}
