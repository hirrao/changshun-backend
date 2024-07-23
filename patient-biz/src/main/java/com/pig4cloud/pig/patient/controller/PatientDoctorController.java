package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.dto.HeartRateStatsDTO;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;
import com.pig4cloud.pig.patient.service.PatientDoctorService;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 医患绑定表
 *
 * @author wangwenche
 * @date 2024-07-08 23:56:30
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/patientDoctor")
@Tag(description = "patientDoctor", name = "医患绑定表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatientDoctorController {
	
	private final PatientDoctorService patientDoctorService;

	@Operation(summary = "昨日统计异常患者次数", description = "昨日统计异常患者次数")
	@GetMapping("/blood-pressure/yesterday/abnormal/count/{doctorUid}")
	@PreAuthorize("@pms.hasPermission('patient_patientDoctor_view')")
	public long countYesterdayAbnormalBloodPressureRecords(@PathVariable Long doctorUid) {
		return patientDoctorService.countYesterdayAbnormalBloodPressureRecordsByDoctorId(doctorUid);
	}

	@Operation(summary = "昨日统计次数", description = "昨日统计次数")
	@GetMapping("/blood-pressure/count/{doctorUid}")
	@PreAuthorize("@pms.hasPermission('patient_patientDoctor_view')")
	public long countBloodPressureRecords(@PathVariable Long doctorUid) {
		return patientDoctorService.countBloodPressureRecordsByDoctorId(doctorUid);
	}


	@Operation(summary = "特别关心患者总数", description = "特别关心患者总数")
	@GetMapping("/count/care/{doctorUid}")
	@PreAuthorize("@pms.hasPermission('patient_patientDoctor_view')")
	public long countPatientsWithCare(@PathVariable Long doctorUid) {
		return patientDoctorService.countPatientsByDoctorIdAndCare(doctorUid);
	}

	@Operation(summary = "患者总数", description = "患者总数")
	@GetMapping("/count/{doctorUid}")
	@PreAuthorize("@pms.hasPermission('patient_patientDoctor_view')")
	public long countPatients(@PathVariable Long doctorUid) {
		return patientDoctorService.countPatientsByDoctorId(doctorUid);
	}
	
	
	@Operation(summary = "查询特别关心心率类别人数", description = "查询特别关心心率类别人数")
	@GetMapping("/heart-rate-stats")
	@PreAuthorize("@pms.hasPermission('patient_patientDoctor_view')")
	public HeartRateStatsDTO getHeartRateStats(@RequestParam("doctorUid") Long doctorUid) {
		return patientDoctorService.getHeartRateStats(doctorUid);
	}
	
	/**
	 * 分页查询
	 *
	 * @param page          分页对象
	 * @param patientDoctor 医患绑定表
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('patient_patientDoctor_view')")
	public R getPatientDoctorPage(@ParameterObject Page page,
	 @ParameterObject PatientDoctorEntity patientDoctor) {
		LambdaQueryWrapper<PatientDoctorEntity> wrapper = Wrappers.lambdaQuery();
		return R.ok(patientDoctorService.page(page, wrapper));
	}
	
	@Operation(summary = "全条件查询医生患者绑定信息", description = "全条件查询医生患者绑定信息")
	@PostMapping("/getByPatientDoctor")
	@PreAuthorize("@pms.hasPermission('patient_patientDoctor_view')")
	public R getByPatientDoctorObject(@RequestBody PatientDoctorEntity patientDoctor) {
		LambdaQueryWrapper<PatientDoctorEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.setEntity(patientDoctor);
		return R.ok(patientDoctorService.list(wrapper));
	}
/*
    @Operation(summary = "患者输入医生编号和医生绑定", description = "患者输入医生编号和医生绑定")
    @GetMapping("/{patientUid}/{doctorUid}/binding")
    @PreAuthorize("@pms.hasPermission('patient_userFeedback_view')")
    public R setPatientDoctorBinding(@PathVariable("patientUid") Long patientUid, @PathVariable("doctorUid") Long doctorUid){
        return R.ok(patientDoctorService.setPatientDoctorBinding(patientUid, doctorUid));
    }
*/
	
	/**
	 * 通过id查询医患绑定表
	 *
	 * @param pdId id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{pdId}")
	@PreAuthorize("@pms.hasPermission('patient_patientDoctor_view')")
	public R getById(@PathVariable("pdId") Long pdId) {
		return R.ok(patientDoctorService.getById(pdId));
	}
	
	/**
	 * 新增医患绑定表
	 *
	 * @param patientDoctor 医患绑定表
	 * @return R
	 */
	@Operation(summary = "新增医患绑定表", description = "新增医患绑定表")
	@SysLog("新增医患绑定表")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('patient_patientDoctor_add')")
	public R save(@RequestBody PatientDoctorEntity patientDoctor) {
		return patientDoctorService.addItem(patientDoctor);
	}
	
	/**
	 * 修改医患绑定表
	 *
	 * @param patientDoctor 医患绑定表
	 * @return R
	 */
	@Operation(summary = "修改医患绑定表", description = "修改医患绑定表")
	@SysLog("修改医患绑定表")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('patient_patientDoctor_edit')")
	public R updateById(@RequestBody PatientDoctorEntity patientDoctor) {
		return patientDoctorService.updateItem(patientDoctor);
	}
	
	/**
	 * 通过id删除医患绑定表
	 *
	 * @param ids pdId列表
	 * @return R
	 */
	@Operation(summary = "通过id删除医患绑定表", description = "通过id删除医患绑定表")
	@SysLog("通过id删除医患绑定表")
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('patient_patientDoctor_del')")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(patientDoctorService.removeBatchByIds(CollUtil.toList(ids)));
	}
	
	
	/**
	 * 导出excel 表格
	 *
	 * @param patientDoctor 查询条件
	 * @param ids           导出指定ID
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('patient_patientDoctor_export')")
	public List<PatientDoctorEntity> export(PatientDoctorEntity patientDoctor, Long[] ids) {
		return patientDoctorService.list(Wrappers.lambdaQuery(patientDoctor)
		 .in(ArrayUtil.isNotEmpty(ids), PatientDoctorEntity::getPdId, ids));
	}
}