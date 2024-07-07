package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;
import com.pig4cloud.pig.patient.service.PatientBaseService;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
	

	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('patient_patientBase_view')")
	public R<IPage<PatientBaseEntity>> getPatientPage(
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size,
			PatientDoctorEntity patientDoctor) {
		Page<PatientBaseEntity> patientBasePage = new Page<>(page, size);
		IPage<PatientBaseEntity> resultPage = patientBaseService.getPatientPage(patientBasePage, patientDoctor);
		return R.ok(resultPage);
	}
	//public R getPatientBasePage(@ParameterObject Page page,
								//@ParameterObject PatientBaseEntity patientBase, PatientDoctorEntity patientDoctor) {
		//LambdaQueryWrapper<PatientBaseEntity> wrapper = Wrappers.lambdaQuery();
		//return R.ok(patientBaseService.page(page, wrapper));
		//return R.ok(patientBaseService.getPatientPage(page, patientDoctor));
		//Page<PatientBaseEntity> patientBasePage = new Page<>(page, size);
		//IPage<PatientBaseEntity> resultPage = patientBaseService.getPatientPage(patientBasePage, patientDoctor);
		//return R.ok(resultPage);

	
	
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
	@Operation(summary = "新增患者基本信息", description = "新增患者基本信息")
	@SysLog("新增患者基本信息")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('patient_patientBase_add')")
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
}