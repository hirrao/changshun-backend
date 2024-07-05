package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.entity.DrugEatTimeEntity;
import com.pig4cloud.pig.patient.entity.UserFeedbackEntity;
import com.pig4cloud.pig.patient.service.DrugEatTimeService;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * 用药时间对照表（因为一款药可能需要多个时间）
 *
 * @author 袁钰涛
 * @date 2024-07-05 09:57:07
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/drugEatTime")
@Tag(description = "drugEatTime", name = "用药时间对照表（因为一款药可能需要多个时间）管理")
@Slf4j
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class DrugEatTimeController {
	
	private final DrugEatTimeService drugEatTimeService;
	
	@Operation(summary = "查询用药时间", description = "查询用药时间")
	@PostMapping("/getByUserDrugEatTime")
	@PreAuthorize("@pms.hasPermission('patient_userDrugEatTime_view')")
	public R getByUserFeedbackObject(@RequestBody DrugEatTimeEntity userDrugEatTime) {
		LambdaQueryWrapper<DrugEatTimeEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.setEntity(userDrugEatTime);
		return R.ok(drugEatTimeService.list(wrapper));
	}
	
	/**
	 * 描述: 查询某个患者当天用药以及是否已经服用
	 */
	@Operation(summary = "查询某个患者当天用药情况", description = "查询某个患者当天用药情况")
	@GetMapping("/getTodayDrugEatTime/{patientUid}")
	@PreAuthorize("@pms.hasPermission('patient_drugEatTime_view')")
	public R getTodayDrugEatTime(@PathVariable("patientUid") Long patientUid) {
		return R.ok(drugEatTimeService.getTodayDrugEatTime(patientUid));
	}
	
	/**
	 * 描述: 服用某个用药计划中的某个时间段请求，正常只需传入pepId即可
	 */
	@Operation(summary = "服用某个用药计划中的某个时间段", description = "服用某个用药计划中的某个时间段")
	@PostMapping("/eatDrug")
	@PreAuthorize("@pms.hasPermission('patient_drugEatTime_edit')")
	public R eatDrug(@RequestBody DrugEatTimeEntity drugEatTime) {
		try {
			drugEatTimeService.eatDrug(drugEatTime);
			return R.ok();
		} catch (Exception e) {
			log.error("提交服药请求失败", e);
			return R.failed();
		}
	}
	
	/**
	 * 分页查询
	 *
	 * @param page        分页对象
	 * @param drugEatTime 用药时间对照表（因为一款药可能需要多个时间）
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('patient_drugEatTime_view')")
	public R getDrugEatTimePage(@ParameterObject Page page,
	 @ParameterObject DrugEatTimeEntity drugEatTime) {
		LambdaQueryWrapper<DrugEatTimeEntity> wrapper = Wrappers.lambdaQuery();
		return R.ok(drugEatTimeService.page(page, wrapper));
	}
	
	
	/**
	 * 通过id查询用药时间对照表（因为一款药可能需要多个时间）
	 *
	 * @param pepId id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{pepId}")
	@PreAuthorize("@pms.hasPermission('patient_drugEatTime_view')")
	public R getById(@PathVariable("pepId") Long pepId) {
		return R.ok(drugEatTimeService.getById(pepId));
	}
	
	/**
	 * 新增用药时间对照表（因为一款药可能需要多个时间）
	 *
	 * @param drugEatTime 用药时间对照表（因为一款药可能需要多个时间）
	 * @return R
	 */
	@Operation(summary = "新增用药时间对照表（因为一款药可能需要多个时间）", description = "新增用药时间对照表（因为一款药可能需要多个时间）")
	@SysLog("新增用药时间对照表（因为一款药可能需要多个时间）")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('patient_drugEatTime_add')")
	public R save(@RequestBody DrugEatTimeEntity drugEatTime) {
		return R.ok(drugEatTimeService.save(drugEatTime));
	}
	
	/**
	 * 修改用药时间对照表（因为一款药可能需要多个时间）
	 *
	 * @param drugEatTime 用药时间对照表（因为一款药可能需要多个时间）
	 * @return R
	 */
	@Operation(summary = "修改用药时间对照表（因为一款药可能需要多个时间）", description = "修改用药时间对照表（因为一款药可能需要多个时间）")
	@SysLog("修改用药时间对照表（因为一款药可能需要多个时间）")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('patient_drugEatTime_edit')")
	public R updateById(@RequestBody DrugEatTimeEntity drugEatTime) {
		return R.ok(drugEatTimeService.updateById(drugEatTime));
	}
	
	/**
	 * 通过id删除用药时间对照表（因为一款药可能需要多个时间）
	 *
	 * @param ids pepId列表
	 * @return R
	 */
	@Operation(summary = "通过id删除用药时间对照表（因为一款药可能需要多个时间）", description = "通过id删除用药时间对照表（因为一款药可能需要多个时间）")
	@SysLog("通过id删除用药时间对照表（因为一款药可能需要多个时间）")
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('patient_drugEatTime_del')")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(drugEatTimeService.removeBatchByIds(CollUtil.toList(ids)));
	}
	
	
	/**
	 * 导出excel 表格
	 *
	 * @param drugEatTime 查询条件
	 * @param ids         导出指定ID
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('patient_drugEatTime_export')")
	public List<DrugEatTimeEntity> export(DrugEatTimeEntity drugEatTime, Long[] ids) {
		return drugEatTimeService.list(Wrappers.lambdaQuery(drugEatTime)
		 .in(ArrayUtil.isNotEmpty(ids), DrugEatTimeEntity::getPepId, ids));
	}


	@Operation(summary = "全条件查询反馈信息", description = "全条件查询反馈信息")
	@PostMapping("/getByDrugEatTimeObject")
	@PreAuthorize("@pms.hasPermission('patient_drugEatTime_view')")
	public R getByDrugEatTimeObject(@RequestBody DrugEatTimeEntity drugEatTime) {
		LambdaQueryWrapper<DrugEatTimeEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.setEntity(drugEatTime);
		return R.ok(drugEatTimeService.list(wrapper));
	}

}