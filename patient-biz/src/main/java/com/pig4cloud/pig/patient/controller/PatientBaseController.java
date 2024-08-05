package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.pig.patient.entity.PatientDeviceEntity;
import com.pig4cloud.pig.patient.request.ImportPatientBaseListRequest;
import com.pig4cloud.pig.patient.service.PatientBaseService;
import com.pig4cloud.plugin.excel.annotation.RequestExcel;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * 患者基本信息
 *
 * @author huangjiayu
 * @date 2024-07-03 16:07:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/patientBase")
@Tag(description = "patientBase", name = "患者基本信息管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatientBaseController {
	
	private final PatientBaseService patientBaseService;
	
	@Operation(summary = "查询年龄性别", description = "查询年龄性别")
	@GetMapping("/{doctorUid}/patient/stats")
	@PreAuthorize("@pms.hasPermission('patient_patientBase_view')")
	public R getPatientStatistics(@PathVariable("doctorUid") Long doctorUid) {
		return R.ok(patientBaseService.getPatientStatistics(doctorUid));
	}
	
	@Operation(summary = "查询特别关心年龄性别", description = "查询特别关心年龄性别")
	@GetMapping("/{doctorUid}/patient/bycarestats")
	@PreAuthorize("@pms.hasPermission('patient_patientBase_view')")
	public R getPatientbycareStatistics(@PathVariable("doctorUid") Long doctorUid) {
		return R.ok(patientBaseService.getPatientbycareStatistics(doctorUid));
	}
	
	
	@Operation(summary = "自定义分页查询", description = "分页查询")
	@GetMapping("/page_custom")
	@PreAuthorize("@pms.hasPermission('patient_patientBase_view')")
	public R getPatientBasePageByCare(@ParameterObject Page<?> page) {
		return R.ok(patientBaseService.pageByCare(page));
	}
	
	@Operation(summary = "代码生成分页查询", description = "代码生成分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('patient_patientBase_view')")
	public R getPage(@ParameterObject Page page,
	 @ParameterObject PatientDeviceEntity patientDevice) {
		LambdaQueryWrapper<PatientBaseEntity> wrapper = Wrappers.lambdaQuery();
		return R.ok(patientBaseService.page(page, wrapper));
		
	}
	
	@Operation(summary = "全条件查询患者信息", description = "全条件查询患者信息")
	@PostMapping("/getByPatientBaseObject")
	@PreAuthorize("@pms.hasPermission('patient_patientBase_view')")
	public R getByPatientBaseObject(@RequestBody PatientBaseEntity patientBase) {
		LambdaQueryWrapper<PatientBaseEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.setEntity(patientBase);
		return R.ok(patientBaseService.list(wrapper));
	}

	@Operation(summary = "修改患者体力状况", description = "修改患者体力状况")
	@PutMapping("/edit_physical_strength")
	@PreAuthorize("@pms.hasPermission('patient_patientBase_edit')")
	public R editPhysicalStrength(@RequestParam Long patientUid, @RequestParam int physicalStrength) {
		String result = patientBaseService.editPhysicalStrength(patientUid, physicalStrength);
		if ("success".equals(result)) {
			return R.ok();
		} else {
			return R.failed(result);
		}
	}

	@Operation(summary = "查询患者姓名性别年龄", description = "查询患者姓名性别年龄")
	@GetMapping("/get_name_sex_age")
	@PreAuthorize("@pms.hasPermission('patient_patientBase_view')")
	public R getPatientNameSexAge(@RequestParam Long patientUid) {
		return R.ok(patientBaseService.getPatientNameSexAge(patientUid));
	}


	/**
	 * 通过id查询患者基本信息
	 *
	 * @param patientUid id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{patientUid}")
	// note: 下面字符串就是接口具有的权限，这个权限是在数据库中配置的
	@PreAuthorize("@pms.hasPermission('patient_patientBase_view')")
	public R getById(@PathVariable("patientUid") Long patientUid) {
		return R.ok(patientBaseService.getById(patientUid));
	}
	
	/**
	 * 新增患者基本信息
	 *
	 * @param patientBase 患者基本信息
	 * @return R
	 */
	// 添加患者基本信息不能具有权限，保证外部可以添加
	@Inner(value = false)
	@Operation(summary = "新增患者基本信息", description = "新增患者基本信息")
	@SysLog("新增患者基本信息")
	@PostMapping
	//@PreAuthorize("@pms.hasPermission('patient_patientBase_add')")
	public R save(@RequestBody PatientBaseEntity patientBase) {
		return R.ok(patientBaseService.save(patientBase));
	}
	
	/**
	 * 修改患者基本信息
	 *
	 * @param patientBase 患者基本信息
	 * @return R
	 */
	@Operation(summary = "修改患者基本信息", description = "修改患者基本信息")
	@SysLog("修改患者基本信息")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('patient_patientBase_edit')")
	public R updateById(@RequestBody PatientBaseEntity patientBase) {
		return R.ok(patientBaseService.updateById(patientBase));
	}
	
	/**
	 * 通过id删除患者基本信息
	 *
	 * @param ids patientUid列表
	 * @return R
	 */
	@Operation(summary = "通过id删除患者基本信息", description = "通过id删除患者基本信息")
	@SysLog("通过id删除患者基本信息")
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('patient_patientBase_del')")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(patientBaseService.removeBatchByIds(CollUtil.toList(ids)));
	}
	
	
	/**
	 * 导出excel 表格
	 *
	 * @param patientBase 查询条件
	 * @param ids         导出指定ID
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('patient_patientBase_export')")
	public List<PatientBaseEntity> export(PatientBaseEntity patientBase, Long[] ids) {
		return patientBaseService.list(Wrappers.lambdaQuery(patientBase)
		 .in(ArrayUtil.isNotEmpty(ids), PatientBaseEntity::getPatientUid, ids));
	}
	
	/**
	 * 通过Excel批量导入患者病历，仅支持xls、xlsx格式
	 */
	@Operation(summary = "通过Excel批量导入患者病例", description = "通过Excel批量导入患者病例")
	@PostMapping("/import")
	@PreAuthorize("@pms.hasPermission('patient_patientBase_export')")
	public R importPatientCase(@RequestExcel List<ImportPatientBaseListRequest> excelList,
	 BindingResult bindingResult) {
		return patientBaseService.importPatientBaseList(excelList, bindingResult);
	}
	
}