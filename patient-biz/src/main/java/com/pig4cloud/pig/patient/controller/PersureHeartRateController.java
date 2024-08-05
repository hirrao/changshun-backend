package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.entity.HeartRateLogsEntity;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import com.pig4cloud.pig.patient.service.PersureHeartRateService;
import com.pig4cloud.pig.patient.service.impl.PressureAnomalyServiceImpl;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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

    @Operation(summary = "查询最近十天血压统计次数" , description = "查询最近十天血压统计次数" )
    @GetMapping("/recent-ten-days/{doctorUid}")
    @PreAuthorize("@pms.hasPermission('patient_persurecount_view')" )
    public Map<String, Long> getDailyStatistics(@PathVariable Long doctorUid) {
        return persureHeartRateService.getDailyStatistics(doctorUid);
    }

    @Operation(summary = "查询特别关心高血压病情" , description = "查询特别关心高血压病情" )
    @GetMapping("/count-sdh-careclassification")
    @PreAuthorize("@pms.hasPermission('patient_persureCareRate_view')" )
    public R countSdhClassification(@RequestParam("doctorUid") Long doctorUid) {
        JSONObject result = persureHeartRateService.countSdhClassificationByDoctorAndCare(doctorUid);
        return R.ok(result);
    }

    @Operation(summary = "查询高血压病情" , description = "查询高血压病情" )
    @GetMapping("/count-sdh-classification")
    @PreAuthorize("@pms.hasPermission('patient_persureRate_view')" )
    public R nocountSdhClassification(@RequestParam("doctorUid") Long doctorUid) {
        JSONObject result = persureHeartRateService.nocountSdhClassificationByDoctorAndCare(doctorUid);
        return R.ok(result);
    }

//    @Operation(summary = "查询心率类型人数" , description = "查询心率类型人数" )
//    @GetMapping("/{doctorUid}/patient/heartRate/stats")
//    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')" )
//    public R getHeartRateStatistics(@PathVariable("doctorUid") Long doctorUid) {
//        JSONObject result = persureHeartRateService.getHeartRateStatistics(doctorUid);
//        return R.ok(result);
//    }

//    @Operation(summary = "查询特别关系心率类型人数" , description = "查询特别关系心率类型人数" )
//    @GetMapping("/{doctorUid}/carepatient/heartRate/stats")
//    @PreAuthorize("@pms.hasPermission('patient_persureHeartCare_view')" )
//    public R getcareHeartRateStatistics(@PathVariable("doctorUid") Long doctorUid) {
//        JSONObject result = persureHeartRateService.getcareHeartRateStatistics(doctorUid);
//        return R.ok(result);
//    }


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
    @Operation(summary = "获取最近一次的风险评估信息", description = "获取当前风险评估信息")
    @PostMapping("/pressureAnomalyLogs")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getCurrentRiskAssessment(@RequestParam("patientUid") Long patientUid){
        return R.ok(persureHeartRateService.getCurrentRiskAssessment(patientUid));
    }

    @Operation(summary = "查询某一周的所有高压、低压", description = "查询某一周的所有高压、低压")
    @GetMapping("/weeklyPressureData")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getWeeklyPressureData(@RequestParam int weeksAgo, @RequestParam Long patientUid) {
        JSONArray result = persureHeartRateService.getWeeklyPressureData(weeksAgo, patientUid);
        return R.ok(result);
    }

    @Operation(summary = "查询某一个月的所有高压、低压", description = "查询某一个月的所有高压、低压")
    @GetMapping("/monthlyPressureData")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getMonthlyPressureData(@RequestParam int monthsAgo, @RequestParam Long patientUid) {
        JSONArray result = persureHeartRateService.getMonthlyPressureData(monthsAgo, patientUid);
        return R.ok(result);
    }

    @Operation(summary = "查询某一年的所有高压、低压", description = "查询某一年的所有高压、低压")
    @GetMapping("/yearlyPressureData")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getYearlyPressureData(@RequestParam int yearsAgo, @RequestParam Long patientUid) {
        JSONArray result = persureHeartRateService.getYearlyPressureData(yearsAgo, patientUid);
        return R.ok(result);
    }

//    @Operation(summary = "统计今天连续高血压和低心率的患者", description = "统计今天所有患者连续高血压或低心率的次数")
//    @GetMapping("/todayConsecutiveAbnormalities")
//    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
//    public R getTodayConsecutiveAbnormalities(@RequestParam Long doctorUid) {
//        JSONArray result = persureHeartRateService.getDailyConsecutiveAbnormalities(doctorUid);
//        return R.ok(result);
//    }

    @Operation(summary = "统计今天收缩压的最高最低和平均值", description = "统计今天收缩压的最高最低和平均值")
    @GetMapping("/todayMaxMinAvgSystolic")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getDailyMaxMinAvgSystolic(@RequestParam Long patientUid) {
        JSONObject result = persureHeartRateService.getDailyMaxMinAvgSystolic(patientUid);
        return R.ok(result);
    }

    @Operation(summary = "统计今天舒张压的最高最低和平均值", description = "统计今天舒张压的最高最低和平均值")
    @GetMapping("/todayMaxMinAvgDiastolic")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getDailyMaxMinAvgDiastolic(@RequestParam Long patientUid) {
        JSONObject result = persureHeartRateService.getDailyMaxMinAvgDiastolic(patientUid);
        return R.ok(result);
    }

    @Operation(summary = "统计本周收缩压的最高最低和平均值", description = "统计本周收缩压的最高最低和平均值")
    @GetMapping("/WeekMaxMinAvgSystolic")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getWeeklyMaxMinAvgSystolic(@RequestParam Long patientUid) {
        JSONObject result = persureHeartRateService.getWeeklyMaxMinAvgSystolic(patientUid);
        return R.ok(result);
    }

    @Operation(summary = "统计本周舒张压的最高最低和平均值", description = "统计本周舒张压的最高最低和平均值")
    @GetMapping("/WeekMaxMinAvgDiastolic")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getWeeklyMaxMinAvgDiastolic(@RequestParam Long patientUid) {
        JSONObject result = persureHeartRateService.getWeeklyMaxMinAvgDiastolic(patientUid);
        return R.ok(result);
    }

    @Operation(summary = "统计本月收缩压的最高最低和平均值", description = "统计本月收缩压的最高最低和平均值")
    @GetMapping("/MonthMaxMinAvgSystolic")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getMonthlyMaxMinAvgSystolic(@RequestParam Long patientUid) {
        JSONObject result = persureHeartRateService.getMonthlyMaxMinAvgSystolic(patientUid);
        return R.ok(result);
    }

    @Operation(summary = "统计本月舒张压的最高最低和平均值", description = "统计本月舒张压的最高最低和平均值")
    @GetMapping("/MonthMaxMinAvgDiastolic")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getMonthlyMaxMinAvgDiastolic(@RequestParam Long patientUid) {
        JSONObject result = persureHeartRateService.getMonthlyMaxMinAvgDiastolic(patientUid);
        return R.ok(result);
    }

    @Operation(summary = "统计今年收缩压的最高最低和平均值", description = "统计今年收缩压的最高最低和平均值")
    @GetMapping("/YearMaxMinAvgSystolic")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getYearlyMaxMinAvgSystolic(@RequestParam Long patientUid) {
        JSONObject result = persureHeartRateService.getYearlyMaxMinAvgSystolic(patientUid);
        return R.ok(result);
    }

    @Operation(summary = "统计今年舒张压的最高最低和平均值", description = "统计今年舒张压的最高最低和平均值")
    @GetMapping("/YearMaxMinAvgDiastolic")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getYearlyMaxMinAvgDiastolic(@RequestParam Long patientUid) {
        JSONObject result = persureHeartRateService.getYearlyMaxMinAvgDiastolic(patientUid);
        return R.ok(result);
    }

    @Operation(summary = "统计今天脉压差的最高最低和平均值", description = "统计今天脉压差的最高最低和平均值")
    @GetMapping("/todayMaxMinAvgPressureDiff")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getDailyMaxMinAvgPressureDiff(@RequestParam Long patientUid) {
        JSONObject result = persureHeartRateService.getDailyMaxMinAvgPressureDiff(patientUid);
        return R.ok(result);
    }

    @Operation(summary = "统计本周脉压差的最高最低和平均值", description = "统计本周脉压差的最高最低和平均值")
    @GetMapping("/WeekMaxMinAvgPressureDiff")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getWeeklyMaxMinAvgPressureDiff(@RequestParam Long patientUid) {
        JSONObject result = persureHeartRateService.getWeeklyMaxMinAvgPressureDiff(patientUid);
        return R.ok(result);
    }

    @Operation(summary = "统计本月脉压差的最高最低和平均值", description = "统计本月脉压差的最高最低和平均值")
    @GetMapping("/MonthMaxMinAvgPressureDiff")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getMonthlyMaxMinAvgPressureDiff(@RequestParam Long patientUid) {
        JSONObject result = persureHeartRateService.getMonthlyMaxMinAvgPressureDiff(patientUid);
        return R.ok(result);
    }

    @Operation(summary = "统计今年脉压差的最高最低和平均值", description = "统计今年脉压差的最高最低和平均值")
    @GetMapping("/YearMaxMinAvgPressureDiff")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getYearlyMaxMinAvgPressureDiff(@RequestParam Long patientUid) {
        JSONObject result = persureHeartRateService.getYearlyMaxMinAvgPressureDiff(patientUid);
        return R.ok(result);
    }

//    @Operation(summary = "查询某一天的某位患者的高压、低压、心率的平均值", description = "查询某一天的某位患者的高压和低压的平均值")
//    @GetMapping("/dailyAvgPressure")
//    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
//    public R getDailyAveragePressure(@RequestParam LocalDate date, @RequestParam Long patientUid) {
//        JSONObject result = persureHeartRateService.getDailyAveragePressureHeartRate(date, patientUid);
//        return R.ok(result);
//    }

//    @Operation(summary = "查询某一周的某位患者的高压、低压、心率的平均值", description = "查询某一周的某位患者的高压、低压、心率的平均值")
//    @GetMapping("/weeklyAvgPressureByDay")
//    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
//    public R getWeeklyAveragePressureByDay(@RequestParam int weeksAgo, @RequestParam Long patientUid) {
//        return R.ok(persureHeartRateService.getWeeklyAveragePressureHeartRateByDay(weeksAgo, patientUid));
//    }

//    @Operation(summary = "查询某一个月的某位患者的每周段的高压、低压、心率的平均值", description = "查询某一个月的某位患者的每周段的高压、低压、心率的平均值")
//    @GetMapping("/monthlyAvgPressureByWeek")
//    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
//    public R getMonthlyAveragePressureByWeek(@RequestParam int monthsAgo, @RequestParam Long patientUid) {
//        return R.ok(persureHeartRateService.getMonthlyAveragePressureHeartRateByWeek(monthsAgo, patientUid));
//    }

//    @Operation(summary = "查询某一年的每个月的高压、低压、心率的平均值", description = "查询某一年的每个月的高压、低压、心率的平均值")
//    @GetMapping("/yearlyAveragePressureByMonth")
//    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
//    public R getYearlyAveragePressureByMonth(@RequestParam int yearsAgo, @RequestParam Long patientUid) {
//        return R.ok(persureHeartRateService.getYearlyAveragePressureHeartRateByMonth(yearsAgo, patientUid));
//    }

    @Operation(summary = "查询最新的血压和心率数据", description = "查询最新的血压和心率数据")
    @GetMapping("/getNewlyPressureHeartRateData")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getNewlyPressureHeartRateData(@RequestParam Long patientUid) {
        return R.ok(persureHeartRateService.getNewlyPressureHeartRateData(patientUid));
    }

    // 以下接口来自原数据库血压异常记录统计表 pressure_anomaly_logs
    @Operation(summary = "查询某一天的风险等级次数统计", description = "查询某一天的风险等级次数统计")
    @GetMapping("/getRiskNum")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getRiskAssessmentNum(@RequestParam Long patientUid, @RequestParam LocalDate date) {
        return R.ok(persureHeartRateService.getRiskAssessmentNum(patientUid, date));
    }

    @Operation(summary = "查询某患者最近7天的血压异常次数", description = "查询某患者最近7天的血压异常次数")
    @GetMapping("/lastSevenDayAnomaly")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_view')")
    public R getLastSevenDayAnomalyNum(@RequestParam Long patientUid) {
        return R.ok(persureHeartRateService.getLastSevenDayAnomalyNum(patientUid));
    }

    @Operation(summary = "查询某患者指定周的血压异常次数", description = "查询某患者指定周的血压异常次数")
    @GetMapping("/weekAnomalyCount")
    @PreAuthorize("@pms.hasPermission('patient_pressureAnomaly_view')")
    public R getWeekAnomalyCount(@RequestParam Long patientUid, @RequestParam int weeksAgo){
        JSONObject result = persureHeartRateService.getWeekAnomalyCount(patientUid, weeksAgo);
        return R.ok(result);
    }

//    @Operation(summary = "医生端获取特别关心患者的血压异常统计", description = "医生端获取特别关心患者的血压异常统计")
//    @GetMapping("/getAnomalyStatsByDoctorUidCare")
//    @PreAuthorize("@pms.hasPermission('patient_pressureAnomaly_view')")
//    public R getAnomalyStatsByDoctorUidCare(@RequestParam Long doctorUid) {
//        // JSONArray
//    }

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
    public R save(@Valid @RequestBody PersureHeartRateEntity persureHeartRate) {
        boolean saved = persureHeartRateService.savePersureHeartRate(persureHeartRate);
        if (saved) {
            persureHeartRateService.updateSdhClassification(persureHeartRate.getSdhId(), persureHeartRate.getPatientUid());
        }
        return R.ok(saved);
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
    public R updateById(@Valid @RequestBody PersureHeartRateEntity persureHeartRate) {
        boolean updated = persureHeartRateService.updateById(persureHeartRate);
        if (updated) {
            persureHeartRateService.updateSdhClassification(persureHeartRate.getSdhId(), persureHeartRate.getPatientUid());
        }
        return R.ok(updated);
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

    @Operation(summary = "批量添加血压数据", description = "批量添加血压数据")
    @PostMapping("/AddPressureInBatches")
    @PreAuthorize("@pms.hasPermission('patient_persureHeartRate_add')")
    public R saveInBatches(@RequestBody List<PersureHeartRateEntity> result) {
        for(PersureHeartRateEntity data : result) {
            boolean updated = persureHeartRateService.updateById(data);
            if (updated) {
                persureHeartRateService.updateSdhClassification(data.getSdhId(), data.getPatientUid());
            }
        }
        return R.ok();
    }
}