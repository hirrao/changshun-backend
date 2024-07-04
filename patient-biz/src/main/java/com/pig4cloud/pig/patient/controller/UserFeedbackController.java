package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.entity.UserFeedbackEntity;
import com.pig4cloud.pig.patient.service.UserFeedbackService;
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
 * 用户反馈信息管理
 *
 * @author huangjiayu
 * @date 2024-07-04 15:59:24
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/userFeedback")
@Tag(description = "userFeedback", name = "用户反馈信息管理管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class UserFeedbackController {
	
	private final UserFeedbackService userFeedbackService;
	
	/**
	 * 分页查询
	 *
	 * @param page         分页对象
	 * @param userFeedback 用户反馈信息管理
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('patient_userFeedback_view')")
	public R getUserFeedbackPage(@ParameterObject Page page,
	 @ParameterObject UserFeedbackEntity userFeedback) {
		LambdaQueryWrapper<UserFeedbackEntity> wrapper = Wrappers.lambdaQuery();
		return R.ok(userFeedbackService.page(page, wrapper));
	}
	
	
	/**
	 * 通过id查询用户反馈信息管理
	 *
	 * @param prrId id
	 * @return R
	 */
	@Operation(summary = "通过表唯一id查询", description = "通过id查询")
	@GetMapping("/{prrId}")
	@PreAuthorize("@pms.hasPermission('patient_userFeedback_view')")
	public R getById(@PathVariable("prrId") Long prrId) {
		return R.ok(userFeedbackService.getById(prrId));
	}
	
	@Operation(summary = "通过患者id查询", description = "通过患者id查询")
	@GetMapping("/getByPatientUId/{patientUId}")
	@PreAuthorize("@pms.hasPermission('patient_userFeedback_view')")
	public R getByPatientId(@PathVariable("patientUId") Long patientId) {
		LambdaQueryWrapper<UserFeedbackEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(UserFeedbackEntity::getPatientUid, patientId);
		return R.ok(userFeedbackService.list(wrapper));
	}
	
	@Operation(summary = "全条件查询反馈信息", description = "全条件查询反馈信息")
	@PostMapping("/getByUserFeedbackObject")
	@PreAuthorize("@pms.hasPermission('patient_userFeedback_view')")
	public R getByUserFeedbackObject(@RequestBody UserFeedbackEntity userFeedback) {
		LambdaQueryWrapper<UserFeedbackEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.setEntity(userFeedback);
		return R.ok(userFeedbackService.list(wrapper));
	}
	
	
	/**
	 * 新增用户反馈信息管理
	 *
	 * @param userFeedback 用户反馈信息管理
	 * @return R
	 */
	@Operation(summary = "新增用户反馈信息管理", description = "新增用户反馈信息管理")
	@SysLog("新增用户反馈信息管理")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('patient_userFeedback_add')")
	public R save(@RequestBody UserFeedbackEntity userFeedback) {
		return R.ok(userFeedbackService.save(userFeedback));
	}
	
	/**
	 * 修改用户反馈信息管理
	 *
	 * @param userFeedback 用户反馈信息管理
	 * @return R
	 */
	@Operation(summary = "修改用户反馈信息管理", description = "修改用户反馈信息管理")
	@SysLog("修改用户反馈信息管理")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('patient_userFeedback_edit')")
	public R updateById(@RequestBody UserFeedbackEntity userFeedback) {
		return R.ok(userFeedbackService.updateById(userFeedback));
	}
	
	/**
	 * 通过id删除用户反馈信息管理
	 *
	 * @param ids prrId列表
	 * @return R
	 */
	@Operation(summary = "通过id删除用户反馈信息管理", description = "通过id删除用户反馈信息管理")
	@SysLog("通过id删除用户反馈信息管理")
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('patient_userFeedback_del')")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(userFeedbackService.removeBatchByIds(CollUtil.toList(ids)));
	}
	
	
	/**
	 * 导出excel 表格
	 *
	 * @param userFeedback 查询条件
	 * @param ids          导出指定ID
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@PreAuthorize("@pms.hasPermission('patient_userFeedback_export')")
	public List<UserFeedbackEntity> export(UserFeedbackEntity userFeedback, Long[] ids) {
		return userFeedbackService.list(Wrappers.lambdaQuery(userFeedback)
		 .in(ArrayUtil.isNotEmpty(ids), UserFeedbackEntity::getPrrId, ids));
	}
}