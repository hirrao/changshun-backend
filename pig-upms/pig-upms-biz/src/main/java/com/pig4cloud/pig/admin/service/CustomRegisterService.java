package com.pig4cloud.pig.admin.service;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONPOJOBuilder;
import com.alibaba.nacos.api.naming.pojo.healthcheck.impl.Http;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.entity.SysDept;
import com.pig4cloud.pig.admin.api.entity.SysPost;
import com.pig4cloud.pig.admin.api.entity.SysRole;
import com.pig4cloud.pig.admin.api.entity.SysUser;
import com.pig4cloud.pig.admin.api.vo.DoctorExcelVO;
import com.pig4cloud.pig.admin.api.vo.UserExcelVO;
import com.pig4cloud.pig.common.core.exception.ErrorCodes;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.plugin.excel.vo.ErrorMessage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

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
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@Autowired
	private SysDeptService sysDeptService;
	
	@Autowired
	private SysPostService sysPostService;
	
	@Value("${web.doctor_url}")
	private String doctorUrl;
	
	// 新建库之后将患者角色信息和普通医生id写在下面
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
	
	public R importDoctor(List<DoctorExcelVO> excelVOList,
			BindingResult bindingResult) {
		// 通用校验获取失败的数据
		List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();
		
		// 执行数据插入操作 组装 UserDto
		for (DoctorExcelVO excel : excelVOList) {
			// 个性化校验逻辑
			List<SysUser> userList = sysUserService.list();
			Set<String> errorMsg = new HashSet<>();
			// 校验用户名/手机是否存在，医生以手机号作为用户名
			boolean exsitUserName = userList.stream()
					.anyMatch(sysUser -> excel.getPhone().equals(sysUser.getUsername()));
			if (exsitUserName) {
				errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERNAME_EXISTING,
						excel.getPhone()));
			}
			
			// 数据合法情况
			if (CollUtil.isEmpty(errorMsg)) {
				insertExcelDoctor(excel);
			}
			else {
				// 数据不合法情况
				errorMessageList.add(new ErrorMessage(excel.getLineNum(), errorMsg));
			}
			
		}
		if (CollUtil.isNotEmpty(errorMessageList)) {
			return R.failed(errorMessageList);
		}
		return R.ok();
	}
	
	public void insertExcelDoctor(DoctorExcelVO excel) {
		// 设置角色列表只有医生角色一个人
		List<Long> roleCollList = new ArrayList<>();
		LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = Wrappers.lambdaQuery();
		sysRoleLambdaQueryWrapper.eq(SysRole::getRoleId,DOCTOR_ROLE_ID);
		SysRole doctorRole = sysRoleService.getOne(sysRoleLambdaQueryWrapper.last("limit 1"));
		if (doctorRole == null) {
			throw new RuntimeException("医生RoleId不存在");
		}
		roleCollList.add(doctorRole.getRoleId());
		// 设置pig登录账号中部门和岗位都是默认的
		LambdaQueryWrapper<SysDept> deptLambdaQueryWrapper = Wrappers.lambdaQuery();
		deptLambdaQueryWrapper.eq(SysDept::getDeptId,1);
		SysDept dept = sysDeptService.getOne(deptLambdaQueryWrapper.last("limit 1"));
		LambdaQueryWrapper<SysPost> postLambdaQueryWrapper = Wrappers.lambdaQuery();
		postLambdaQueryWrapper.eq(SysPost::getPostId,1);
		SysPost sysPost = sysPostService.getOne(postLambdaQueryWrapper.last("limit 1"));
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(excel.getPhone());
		userDTO.setPhone(excel.getPhone());
		userDTO.setName(excel.getName());
		// 批量导入初始密码为手机号
		userDTO.setPassword(userDTO.getPhone());
		userDTO.setDeptId(dept.getDeptId());
		List<Long> postIdList = new ArrayList<>();
		postIdList.add(sysPost.getPostId());
		userDTO.setPost(postIdList);
		userDTO.setRole(roleCollList);
		//	插入用户
		sysUserService.saveUser(userDTO);
		
		//	插入doctor_base表格当中
		String url = doctorUrl + "/batchadd";
		JSONObject doctorBase = new JSONObject();
		doctorBase.put("doctorName",excel.getName());
		doctorBase.put("doctorPhonenumber",excel.getPhone());
		doctorBase.put("position",excel.getPosition());
		doctorBase.put("affiliatedHospital",excel.getAffiliatedHospital());
		doctorBase.put("department",excel.getDepartment());
		doctorBase.put("username",excel.getPhone());
		JSONArray params = new JSONArray();
		params.add(doctorBase);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<JSONArray> request = new HttpEntity<>(params, headers);
		ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, request,
				JSONObject.class);
		if (response.getStatusCode() != HttpStatus.OK) {
			throw new ResponseStatusException(response.getStatusCode(), "插入医生请求出错");
		}
	}
}
