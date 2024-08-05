package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.entity.PatientDeviceEntity;
import com.pig4cloud.pig.patient.service.PatientDeviceService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangwenche
 * @date 2024-07-04 20:47:58
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/patientDevice")
@Tag(description = "patientDevice", name = "管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatientDeviceController {
	
	private final PatientDeviceService patientDeviceService;
	
	/**
	 * 分页查询
	 *
	 * @param page          分页对象
	 * @param patientDevice
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('patient_patientDevice_view')")
	public R getPatientDevicePage(@ParameterObject Page page,
	 @ParameterObject PatientDeviceEntity patientDevice) {
		LambdaQueryWrapper<PatientDeviceEntity> wrapper = Wrappers.lambdaQuery();
		return R.ok(patientDeviceService.page(page, wrapper));
	}
	
	
	@Operation(summary = "全条件查询患者设备信息", description = "全条件查询患者设备信息")
	@PostMapping("/getByPatientDeviceObject")
	@PreAuthorize("@pms.hasPermission('patient_patientDevice_view')")
	public R getByUserFeedbackObject(@RequestBody PatientDeviceEntity patientDevice) {
		LambdaQueryWrapper<PatientDeviceEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.setEntity(patientDevice);
		return R.ok(patientDeviceService.list(wrapper));
	}
	
	/**
	 * 通过id查询
	 *
	 * @param pddId id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{pddId}")
	@PreAuthorize("@pms.hasPermission('patient_patientDevice_view')")
	public R getById(@PathVariable("pddId") Long pddId) {
		return R.ok(patientDeviceService.getById(pddId));
	}
	
	/**
	 * 新增
	 *
	 * @param patientDevice
	 * @return R
	 */
	@Operation(summary = "新增绑定设备", description = "新增绑定设备")
	@SysLog("新增")
	@PostMapping("/add_device")
	@PreAuthorize("@pms.hasPermission('patient_patientDevice_add')")
	public R save(@RequestBody PatientDeviceEntity patientDevice) {
		return patientDeviceService.addPatientDevice(patientDevice);
	}
	
	@Operation(summary = "取消设备绑定", description = "取消设备绑定")
	@SysLog("解除绑定")
	@PostMapping("/unbind_device")
	@PreAuthorize("@pms.hasPermission('patient_patientDevice_add')")
	public R unbindDevice(@RequestBody PatientDeviceEntity patientDevice) {
		return patientDeviceService.unbindDevice(patientDevice);
	}
	
	@Operation(summary = "同步设备数据", description = "同步设备数据")
	@SysLog("同步设备数据")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('patient_patientDevice_add')")
	public R syncDeviceData(@RequestBody PatientDeviceEntity patientDevice) {
		return R.ok();
	}
	
	/**
	 * 修改
	 *
	 * @param patientDevice
	 * @return R
	 */
	@Operation(summary = "修改", description = "修改")
	@SysLog("修改")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('patient_patientDevice_edit')")
	public R updateById(@RequestBody PatientDeviceEntity patientDevice) {
		return R.ok(patientDeviceService.updateById(patientDevice));
	}
	
	/**
	 * 通过id删除
	 *
	 * @param ids pddId列表
	 * @return R
	 */
	@Operation(summary = "通过id删除", description = "通过id删除")
	@SysLog("通过id删除")
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('patient_patientDevice_del')")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(patientDeviceService.removeBatchByIds(CollUtil.toList(ids)));
	}
	
	
	/**
	 * 导出excel 表格
	 *
	 * @param patientDevice 查询条件
	 * @param ids           导出指定ID
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('patient_patientDevice_export')")
	public List<PatientDeviceEntity> export(PatientDeviceEntity patientDevice, Long[] ids) {
		return patientDeviceService.list(Wrappers.lambdaQuery(patientDevice)
		 .in(ArrayUtil.isNotEmpty(ids), PatientDeviceEntity::getPddId, ids));
	}
}