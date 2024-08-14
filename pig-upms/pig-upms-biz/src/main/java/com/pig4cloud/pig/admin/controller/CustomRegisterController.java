package com.pig4cloud.pig.admin.controller;

import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.vo.DoctorExcelVO;
import com.pig4cloud.pig.admin.api.vo.UserExcelVO;
import com.pig4cloud.pig.admin.service.CustomRegisterService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.plugin.excel.annotation.RequestExcel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h3>changshun-backend</h3>
 *
 * @author HuangJiayu
 * @description <p>个人定义注册用户Controller层</p>
 * @date 2024-07-11 17:21
 **/
@RestController
@RequestMapping("/custom_register")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "register.user", matchIfMissing = true)
public class CustomRegisterController {
	
	@Autowired
	private CustomRegisterService customRegisterService;
	
	
	@Inner(value = false)
	@SysLog("定制化的注册用户")
	@PostMapping("/user")
	public R registerUser(@RequestBody UserDTO userDto) {
		return customRegisterService.customRegisterUser(userDto);
	}
	
	@Inner(value = false)
	@SysLog("批量导入医生用户")
	@PostMapping("/import_doctor")
	public R importDoctor(@RequestExcel List<DoctorExcelVO> excelVOList,
			BindingResult bindingResult) {
		return customRegisterService.importDoctor(excelVOList,bindingResult);
	}
		
	
}
