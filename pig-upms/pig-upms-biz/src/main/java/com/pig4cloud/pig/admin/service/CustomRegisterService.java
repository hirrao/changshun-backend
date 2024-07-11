package com.pig4cloud.pig.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.common.core.util.R;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * <h3>changshun-backend</h3>
 *
 * @author HuangJiayu
 * @description <p>自定义注册用户实现逻辑</p>
 * @date 2024-07-11 17:22
 **/
@Service
@Slf4j
public class CustomRegisterService {
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	// TODO: 新建库之后将患者角色信息和普通医生id写在下面
	// 患者角色id
	private static final Long PATIENT_ROLE_ID = 1811326313985011714L;
	// 普通医生id
	private static final Long DOCTOR_ROLE_ID = 1811326400899379201L;
	// 医生管理员id
	private static final Long DOCTOR_ADMIN_ROLE_ID = 1811326467232296962L;
	
	
	public R customRegisterUser(UserDTO userDTO) {
		try {
			R<Boolean> booleanR = sysUserService.registerUser(userDTO);
			//	如果没有创建成功，则返回失败
			if (!booleanR.getData()) {
				return R.failed(booleanR.getMsg());
			}
			// 根据角色判定是创建医生基本信息还是患者基本信息
			// 首先检查角色列表中，三种角色中任意两种角色不可以同时存在
			long count = userDTO.getRole().stream().filter(roleId -> {
				return roleId.equals(PATIENT_ROLE_ID) || roleId.equals(DOCTOR_ROLE_ID)
						|| roleId.equals(DOCTOR_ADMIN_ROLE_ID);
			}).count();
			if (count > 1) {
				return R.failed("医生、患者、医生管理员三种角色不可以同时存在");
			}
			// 获取具体的角色
			List<Long> collect = userDTO.getRole().stream().filter(roleId -> {
				return roleId.equals(PATIENT_ROLE_ID) || roleId.equals(DOCTOR_ROLE_ID)
						|| roleId.equals(DOCTOR_ADMIN_ROLE_ID);
			}).toList();
			for (Long roleId : collect) {
				//	只需要执行一次
				if (roleId.equals(PATIENT_ROLE_ID)) {
					//	添加患者的登录用户名
					JSONObject requestPost = new JSONObject();
					requestPost.put("username", userDTO.getUsername());
					requestPost.put("phoneNumber", userDTO.getPhone());
					// 添加微信小程序的id
					requestPost.put("wxUid", userDTO.getMiniOpenid());
					// TODO: 调用微服务中的创建患者基本信息
					String url = "http://127.0.0.1:9999/patient/patientBase";
					//	发送HTTP请求
					try {
						R r = restTemplate.postForObject(url, requestPost, R.class);
						return r;
					} catch (Exception e) {
						log.error("调用微服务中的创建患者基本信息失败", e);
						return R.failed("调用微服务中的创建患者基本信息失败");
					}
				} else {
					// TODO: 调用微服务中的创建医生基本信息
					JSONObject requestPost = new JSONObject();
					requestPost.put("username", userDTO.getUsername());
					requestPost.put("doctorPhoneNumber", userDTO.getPhone());
				}
				break;
			}
			return R.ok();
		} catch (Exception e) {
			log.error("注册用户失败", e);
			return R.failed("注册用户失败");
		}
	}
}
