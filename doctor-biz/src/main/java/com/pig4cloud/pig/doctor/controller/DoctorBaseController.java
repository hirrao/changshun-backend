package com.pig4cloud.pig.doctor.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.pig.doctor.entity.DoctorBaseEntity;
import com.pig4cloud.pig.doctor.request.ImportDoctorBaseListRequest;
import com.pig4cloud.pig.doctor.service.DoctorBaseService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 医生基础信息管理
 *
 * @author huangjiayu
 * @date 2024-07-03 17:17:03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/doctorBase")
@Tag(description = "doctorBase", name = "医生基础信息管理管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class DoctorBaseController {
	
	private final DoctorBaseService doctorBaseService;
	
	@Operation(summary = "批量删除", description = "批量删除")
	@DeleteMapping("/batchdel")
	@PreAuthorize("@pms.hasPermission('doctor_doctorBase_del')")
	public R deleteBatch(@RequestBody List<Long> doctorUids) {
		boolean success = doctorBaseService.deleteBatch(doctorUids);
		return success ? R.ok() : R.failed("删除失败");
	}
	
	@Inner(value = false)
	@Operation(summary = "批量增加", description = "批量增加")
	@PostMapping("/batchadd")
	public R addBatch(@RequestBody List<DoctorBaseEntity> result) {
		return R.ok(doctorBaseService.saveBatch(result));
	}
	
	
	@Operation(summary = "批量修改", description = "批量修改")
	@PutMapping("/batchupd")
	@PreAuthorize("@pms.hasPermission('doctor_doctorBase_edit')")
	public R updateBatch(@RequestBody List<DoctorBaseEntity> doctors) {
		boolean success = doctorBaseService.updateBatch(doctors);
		return success ? R.ok() : R.failed("更新失败");
	}
	
	/**
	 * 分页查询
	 *
	 * @param page       分页对象
	 * @param doctorBase 医生基础信息管理
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('doctor_doctorBase_view')")
	public R getDoctorBasePage(@ParameterObject Page page,
	 @ParameterObject DoctorBaseEntity doctorBase) {
		LambdaQueryWrapper<DoctorBaseEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.setEntity(doctorBase);
		return R.ok(doctorBaseService.page(page, wrapper));
	}
	
	
	/**
	 * 通过id查询医生基础信息管理
	 *
	 * @param doctorUid id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{doctorUid}")
	@PreAuthorize("@pms.hasPermission('doctor_doctorBase_view')")
	public R getById(@PathVariable("doctorUid") Long doctorUid) {
		return R.ok(doctorBaseService.getById(doctorUid));
	}
	
	/**
	 * 新增医生基础信息管理
	 *
	 * @param doctorBase 医生基础信息管理
	 * @return R
	 */
	@Operation(summary = "新增医生基础信息管理", description = "新增医生基础信息管理")
	@SysLog("新增医生基础信息管理")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('doctor_doctorBase_add')")
	public R save(@RequestBody DoctorBaseEntity doctorBase) {
		return R.ok(doctorBaseService.save(doctorBase));
	}
	
	/**
	 * 修改医生基础信息管理
	 *
	 * @param doctorBase 医生基础信息管理
	 * @return R
	 */
	@Operation(summary = "修改医生基础信息管理", description = "修改医生基础信息管理")
	@SysLog("修改医生基础信息管理")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('doctor_doctorBase_edit')")
	public R updateById(@RequestBody DoctorBaseEntity doctorBase) {
		return R.ok(doctorBaseService.updateById(doctorBase));
	}
	
	/**
	 * 通过id删除医生基础信息管理
	 *
	 * @param ids doctorUid列表
	 * @return R
	 */
	@Operation(summary = "通过id删除医生基础信息管理", description = "通过id删除医生基础信息管理")
	@SysLog("通过id删除医生基础信息管理")
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('doctor_doctorBase_del')")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(doctorBaseService.removeBatchByIds(CollUtil.toList(ids)));
	}
	
	
	/**
	 * 导出excel 表格
	 *
	 * @param doctorBase 查询条件
	 * @param ids        导出指定ID
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('doctor_doctorBase_export')")
	public List<DoctorBaseEntity> export(DoctorBaseEntity doctorBase, Long[] ids) {
		return doctorBaseService.list(Wrappers.lambdaQuery(doctorBase)
		 .in(ArrayUtil.isNotEmpty(ids), DoctorBaseEntity::getDoctorUid, ids));
	}
	
	@Operation(summary = "全条件查询反馈信息", description = "全条件查询反馈信息")
	@PostMapping("/getByDoctorBaseObject")
	@PreAuthorize("@pms.hasPermission('doctor_doctorBase_view')")
	public R getByUserFeedbackObject(@RequestBody DoctorBaseEntity doctorBase) {
		LambdaQueryWrapper<DoctorBaseEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.setEntity(doctorBase);
		return R.ok(doctorBaseService.list(wrapper));
	}
	
	@Operation(summary = "通过Excel导入医生基本信息", description = "通过Excel导入医生基本信息")
	@PostMapping("/import")
	@PreAuthorize("@pms.hasPermission('doctor_doctorBase_export')")
	public R importDoctorBase(@RequestExcel List<ImportDoctorBaseListRequest> excelList,
	 BindingResult bindingResult) {
		return doctorBaseService.importDoctorBaseList(excelList, bindingResult);
	}
}