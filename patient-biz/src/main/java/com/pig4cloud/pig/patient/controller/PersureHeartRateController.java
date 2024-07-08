package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.entity.PatientNowDiseaseEntity;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import com.pig4cloud.pig.patient.service.PersureHeartRateService;
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
 * 血压心率展示
 *
 * @author wangwenche
 * @date 2024-07-04 20:02:22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/persureHeartRate" )
@Tag(description = "persureHeartRate" , name = "血压心率展示管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PersureHeartRateController {

    private final  PersureHeartRateService persureHeartRateService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param persureHeartRate 血压心率展示
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')" )
    public R getPersureHeartRatePage(@ParameterObject Page page, @ParameterObject PersureHeartRateEntity persureHeartRate) {
        LambdaQueryWrapper<PersureHeartRateEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(persureHeartRateService.page(page, wrapper));
    }

    @Operation(summary = "全条件查询病人血压相关信息（高压，低压，心率）", description = "全条件查询病人血压相关信息（高压，低压，心率）")
    @PostMapping("/getByPresureHeartRateObject")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getByPresureHeartRateObject(@RequestBody PersureHeartRateEntity persureHeartRate) {
        LambdaQueryWrapper<PersureHeartRateEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.setEntity(persureHeartRate);
        return R.ok(persureHeartRateService.list(wrapper));
    }

    // 统计不同分级的血压次数
    @Operation(summary = "统计不同分级的血压次数", description = "统计不同分级的血压次数")
    @GetMapping("/{patientUid}/grading")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R classifyBloodPressure(@PathVariable("patientUid") Long patientUid){
        return R.ok(persureHeartRateService.classifyAllBloodPressure(patientUid));
    }

    // 获取当前风险评估信息
    @Operation(summary = "获取当前风险评估信息", description = "获取当前风险评估信息")
    @PostMapping("/pressureAnomalyLogs")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getCurrentRiskAssessment(@RequestParam("patientUid") Long patientUid){
        return R.ok(persureHeartRateService.getCurrentRiskAssessment(patientUid));
    }

    @Operation(summary = "查询某一周的所有高压和低压", description = "查询某一周的所有高压和低压")
    @GetMapping("/weeklyPressureData")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getWeeklyPressureData(@RequestParam LocalDate date, @RequestParam int weeksAgo) {
        JSONObject result = persureHeartRateService.getWeeklyPressureData(date, weeksAgo);
        return R.ok(result);
    }

    @Operation(summary = "查询某一个月的所有高压和低压", description = "查询某一个月的所有高压和低压")
    @GetMapping("/monthlyPressureData")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getMonthlyPressureData(@RequestParam LocalDate date, @RequestParam int monthsAgo) {
        JSONObject result = persureHeartRateService.getMonthlyPressureData(date, monthsAgo);
        return R.ok(result);
    }

    @Operation(summary = "统计今天连续高血压和低心率的患者", description = "统计今天所有患者连续高血压或低心率的次数")
    @GetMapping("/todayConsecutiveAbnormalities")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getTodayConsecutiveAbnormalities() {
        JSONArray result = persureHeartRateService.getDailyConsecutiveAbnormalities();
        return R.ok(result);
    }

    /**
     * 通过id查询血压心率展示
     * @param sdhId id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{sdhId}" )
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')" )
    public R getById(@PathVariable("sdhId" ) Long sdhId) {
        return R.ok(persureHeartRateService.getById(sdhId));
    }

    /**
     * 新增血压心率展示
     * @param persureHeartRate 血压心率展示
     * @return R
     */
    @Operation(summary = "新增血压心率展示" , description = "新增血压心率展示" )
    @SysLog("新增血压心率展示" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_add')" )
    public R save(@RequestBody PersureHeartRateEntity persureHeartRate) {
        return R.ok(persureHeartRateService.save(persureHeartRate));
    }

    /**
     * 修改血压心率展示
     * @param persureHeartRate 血压心率展示
     * @return R
     */
    @Operation(summary = "修改血压心率展示" , description = "修改血压心率展示" )
    @SysLog("修改血压心率展示" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_edit')" )
    public R updateById(@RequestBody PersureHeartRateEntity persureHeartRate) {
        return R.ok(persureHeartRateService.updateById(persureHeartRate));
    }

    /**
     * 通过id删除血压心率展示
     * @param ids sdhId列表
     * @return R
     */
    @Operation(summary = "通过id删除血压心率展示" , description = "通过id删除血压心率展示" )
    @SysLog("通过id删除血压心率展示" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(persureHeartRateService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param persureHeartRate 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_export')" )
    public List<PersureHeartRateEntity> export(PersureHeartRateEntity persureHeartRate,Long[] ids) {
        return persureHeartRateService.list(Wrappers.lambdaQuery(persureHeartRate).in(ArrayUtil.isNotEmpty(ids), PersureHeartRateEntity::getSdhId, ids));
    }


    @Operation(summary = "患者当天最高血压展示" , description = "患者当天最高血压展示" )
    @GetMapping("/blood-pressure/max/{patientUid}")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_max')" )
    public PersureHeartRateEntity getTodayMaxBloodPressure(@PathVariable("patientUid") Long patientUid) {
        return persureHeartRateService.getTodayMaxBloodPressure(patientUid);
    }

    @Operation(summary = "患者当天最低心率展示" , description = "患者当天最低心率展示" )
    @GetMapping("/heart-rate/min/{patientUid}")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_min')" )
    public PersureHeartRateEntity getTodayMinHeartRate(@PathVariable("patientUid") Long patientUid) {
        return persureHeartRateService.getTodayMinHeartRate(patientUid);
    }

}