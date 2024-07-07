package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.entity.PressureAnomalyLogsEntity;
import com.pig4cloud.pig.patient.service.PressureAnomalyLogsService;
import org.springframework.security.access.prepost.PreAuthorize;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * 血压异常信息统计
 *
 * @author wangwenche
 * @date 2024-07-07 17:44:20
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/pressureAnomalyLogs" )
@Tag(description = "pressureAnomalyLogs" , name = "血压异常信息统计管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PressureAnomalyLogsController {

    private final  PressureAnomalyLogsService pressureAnomalyLogsService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param pressureAnomalyLogs 血压异常信息统计
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomalyLogs_view')" )
    public R getPressureAnomalyLogsPage(@ParameterObject Page page, @ParameterObject PressureAnomalyLogsEntity pressureAnomalyLogs) {
        LambdaQueryWrapper<PressureAnomalyLogsEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(pressureAnomalyLogsService.page(page, wrapper));
    }

    // 创建每天的记录
    @Operation(summary = "创建每天的记录", description = "创建每天的记录")
    @PostMapping("/createDailyRecord")
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomalyLogs_add')" )
    public R createDailyRecord() {
        return R.ok(pressureAnomalyLogsService.createDailyRecord());
    }

    // 更新血压异常次数
    @Operation(summary = "更新血压异常次数", description = "更新血压异常次数")
    @PostMapping("/updateAnomalyCount")
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomalyLogs_edit')" )
    public R updateAnomalyCount(@RequestParam long sdhId){
        return R.ok(pressureAnomalyLogsService.updateAnomalyCount(sdhId));
    }

    @Operation(summary = "全条件查询当前日期的统计数据", description = "全条件查询当前日期的统计数据")
    @PostMapping("/getAnomalyCount")
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomalyLogs_view')")
    public R getByUserFeedbackObject(@RequestBody PressureAnomalyLogsEntity pressureAnomalyLogs) {
        LambdaQueryWrapper<PressureAnomalyLogsEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.setEntity(pressureAnomalyLogs);
        return R.ok(pressureAnomalyLogsService.list(wrapper));
    }


    /**
     * 通过id查询血压异常信息统计
     * @param date id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{date}" )
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomalyLogs_view')" )
    public R getById(@PathVariable("date" ) LocalDate date) {
        return R.ok(pressureAnomalyLogsService.getById(date));
    }

    /**
     * 新增血压异常信息统计
     * @param pressureAnomalyLogs 血压异常信息统计
     * @return R
     */
    @Operation(summary = "新增血压异常信息统计" , description = "新增血压异常信息统计" )
    @SysLog("新增血压异常信息统计" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomalyLogs_add')" )
    public R save(@RequestBody PressureAnomalyLogsEntity pressureAnomalyLogs) {
        return R.ok(pressureAnomalyLogsService.save(pressureAnomalyLogs));
    }


    /**
     * 修改血压异常信息统计
     * @param pressureAnomalyLogs 血压异常信息统计
     * @return R
     */
    @Operation(summary = "修改血压异常信息统计" , description = "修改血压异常信息统计" )
    @SysLog("修改血压异常信息统计" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomalyLogs_edit')" )
    public R updateById(@RequestBody PressureAnomalyLogsEntity pressureAnomalyLogs) {
        return R.ok(pressureAnomalyLogsService.updateById(pressureAnomalyLogs));
    }

    /**
     * 通过id删除血压异常信息统计
     * @param ids date列表
     * @return R
     */
    @Operation(summary = "通过id删除血压异常信息统计" , description = "通过id删除血压异常信息统计" )
    @SysLog("通过id删除血压异常信息统计" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomalyLogs_del')" )
    public R removeById(@RequestBody LocalDate[] ids) {
        return R.ok(pressureAnomalyLogsService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param pressureAnomalyLogs 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomalyLogs_export')" )
    public List<PressureAnomalyLogsEntity> export(PressureAnomalyLogsEntity pressureAnomalyLogs,LocalDate[] ids) {
        return pressureAnomalyLogsService.list(Wrappers.lambdaQuery(pressureAnomalyLogs).in(ArrayUtil.isNotEmpty(ids), PressureAnomalyLogsEntity::getDate, ids));
    }
}