package com.pig4cloud.pig.patient.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.dto.PatientiListDTO;
import com.pig4cloud.pig.patient.service.PatientListService;
import feign.Param;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h3>patient</h3>
 *
 * @author HuangJiayu
 * @description <p>患者列表与患者详情统一控制接口</p>
 * @date 2024-07-17 10:12
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/patientList")
@Tag(description = "patientList", name = "患者列表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatientListController {
	
	@Autowired
	private PatientListService patientListService;
	
	
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view','patient_patientBase_view')")
	public R getPatientList(@ParameterObject Page page,@ParameterObject PatientiListDTO patientiListDTO) {
		return R.ok(patientListService.getPatientList(page,patientiListDTO));
	}
	
	@Operation(summary = "分页查询血压异常记录")
	@GetMapping("/abnormal_page")
	@PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
	public R getAbnormalPage(@ParameterObject Page page) {
		return R.ok();
	}
}
