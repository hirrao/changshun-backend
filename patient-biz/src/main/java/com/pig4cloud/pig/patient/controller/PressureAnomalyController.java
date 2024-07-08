package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.entity.PressureAnomalyEntity;
import com.pig4cloud.pig.patient.service.PressureAnomalyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.server.ConditionalOnManagementPort;
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
 * 血压异常次数统计
 *
 * @author wangwenche
 * @date 2024-07-08 11:20:07
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/pressureAnomaly" )
@Tag(description = "pressureAnomaly" , name = "血压异常次数统计管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PressureAnomalyController {

    private final  PressureAnomalyService pressureAnomalyService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param pressureAnomaly 血压异常次数统计
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomaly_view')" )
    public R getPressureAnomalyPage(@ParameterObject Page page, @ParameterObject PressureAnomalyEntity pressureAnomaly) {
        LambdaQueryWrapper<PressureAnomalyEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(pressureAnomalyService.page(page, wrapper));
    }

    // 创建每天的记录
    @Operation(summary = "创建每天的记录", description = "创建每天的记录")
    @PostMapping("/createDailyRecord")
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomaly_add')" )
    public R createDailyRecord(@RequestParam long patient_uid) {
        return R.ok(pressureAnomalyService.createDailyRecord(patient_uid));
    }

    // 更新血压异常次数
    @Operation(summary = "更新血压异常次数", description = "更新血压异常次数")
    @PostMapping("/updateAnomalyCount")
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomaly_edit')" )
    public R updateAnomalyCount(@RequestParam long sdhId){
        return R.ok(pressureAnomalyService.updateAnomalyCount(sdhId));
    }

    @Operation(summary = "全条件查询当前日期的统计数据", description = "全条件查询当前日期的统计数据")
    @PostMapping("/getAnomalyCount")
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomaly_view')")
    public R getByUserFeedbackObject(@RequestBody PressureAnomalyEntity pressureAnomaly) {
        LambdaQueryWrapper<PressureAnomalyEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.setEntity(pressureAnomaly);
        return R.ok(pressureAnomalyService.list(wrapper));
    }


    /**
     * 通过id查询血压异常次数统计
     * @param paId id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{paId}" )
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomaly_view')" )
    public R getById(@PathVariable("paId" ) Long paId) {
        return R.ok(pressureAnomalyService.getById(paId));
    }

    /**
     * 新增血压异常次数统计
     * @param pressureAnomaly 血压异常次数统计
     * @return R
     */
    @Operation(summary = "新增血压异常次数统计" , description = "新增血压异常次数统计" )
    @SysLog("新增血压异常次数统计" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomaly_add')" )
    public R save(@RequestBody PressureAnomalyEntity pressureAnomaly) {
        return R.ok(pressureAnomalyService.save(pressureAnomaly));
    }

    /**
     * 修改血压异常次数统计
     * @param pressureAnomaly 血压异常次数统计
     * @return R
     */
    @Operation(summary = "修改血压异常次数统计" , description = "修改血压异常次数统计" )
    @SysLog("修改血压异常次数统计" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomaly_edit')" )
    public R updateById(@RequestBody PressureAnomalyEntity pressureAnomaly) {
        return R.ok(pressureAnomalyService.updateById(pressureAnomaly));
    }

    /**
     * 通过id删除血压异常次数统计
     * @param ids paId列表
     * @return R
     */
    @Operation(summary = "通过id删除血压异常次数统计" , description = "通过id删除血压异常次数统计" )
    @SysLog("通过id删除血压异常次数统计" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomaly_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(pressureAnomalyService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param pressureAnomaly 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomaly_export')" )
    public List<PressureAnomalyEntity> export(PressureAnomalyEntity pressureAnomaly,Long[] ids) {
        return pressureAnomalyService.list(Wrappers.lambdaQuery(pressureAnomaly).in(ArrayUtil.isNotEmpty(ids), PressureAnomalyEntity::getPaId, ids));
    }

    // 查询指定周的血压异常次数(如果当周没有任何数据，JSON中会有一个message字段)
    @Operation(summary = "查询指定周的血压异常次数", description= "查询指定周的血压异常次数")
    @GetMapping("/weekAnomalyCount")
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomaly_view')")
    public R getWeekAnomalyCount(@RequestParam LocalDate date, @RequestParam int weeksAgo){
        JSONObject result = pressureAnomalyService.getWeekAnomalyCount(date, weeksAgo);
        return R.ok(result);
    }

    // 查询指定月的血压异常次数
    @Operation(summary = "查询指定月的血压异常次数", description = "查询指定月的各个级别的血压异常次数")
    @GetMapping("/monthAnomalyCount")
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomaly_view')")
    public R getMonthAnomalyCount(@RequestParam LocalDate date, @RequestParam int monthsAgo) {
        JSONObject result = pressureAnomalyService.getMonthAnomalyCount(date, monthsAgo);
        return R.ok(result);
    }

}