package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.entity.HeartRateLogsEntity;
import com.pig4cloud.pig.patient.service.HeartRateLogsService;
import org.springframework.security.access.prepost.PreAuthorize;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 患者心率数据
 *
 * @author wangwenche
 * @date 2024-08-04 11:45:38
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/heartRateLogs" )
@Tag(description = "heartRateLogs" , name = "患者心率数据管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class HeartRateLogsController {

    private final  HeartRateLogsService heartRateLogsService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param heartRateLogs 患者心率数据
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('patient_heartRateLogs_view')" )
    public R getHeartRateLogsPage(@ParameterObject Page page, @ParameterObject HeartRateLogsEntity heartRateLogs) {
        LambdaQueryWrapper<HeartRateLogsEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(heartRateLogsService.page(page, wrapper));
    }


    /**
     * 通过id查询患者心率数据
     * @param hrlId id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{hrlId}" )
    @PreAuthorize("@pms.hasPermission('patient_heartRateLogs_view')" )
    public R getById(@PathVariable("hrlId" ) Long hrlId) {
        return R.ok(heartRateLogsService.getById(hrlId));
    }

    /**
     * 新增患者心率数据
     * @param heartRateLogs 患者心率数据
     * @return R
     */
    @Operation(summary = "新增患者心率数据" , description = "新增患者心率数据" )
    @SysLog("新增患者心率数据" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('patient_heartRateLogs_add')" )
    public R save(@RequestBody HeartRateLogsEntity heartRateLogs) {
        return R.ok(heartRateLogsService.save(heartRateLogs));
    }

    /**
     * 修改患者心率数据
     * @param heartRateLogs 患者心率数据
     * @return R
     */
    @Operation(summary = "修改患者心率数据" , description = "修改患者心率数据" )
    @SysLog("修改患者心率数据" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('patient_heartRateLogs_edit')" )
    public R updateById(@RequestBody HeartRateLogsEntity heartRateLogs) {
        return R.ok(heartRateLogsService.updateById(heartRateLogs));
    }

    /**
     * 通过id删除患者心率数据
     * @param ids hrlId列表
     * @return R
     */
    @Operation(summary = "通过id删除患者心率数据" , description = "通过id删除患者心率数据" )
    @SysLog("通过id删除患者心率数据" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('patient_heartRateLogs_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(heartRateLogsService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param heartRateLogs 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('patient_heartRateLogs_export')" )
    public List<HeartRateLogsEntity> export(HeartRateLogsEntity heartRateLogs,Long[] ids) {
        return heartRateLogsService.list(Wrappers.lambdaQuery(heartRateLogs).in(ArrayUtil.isNotEmpty(ids), HeartRateLogsEntity::getHrlId, ids));
    }

    @Operation(summary = "批量添加心率数据", description = "批量添加心率数据")
    @PostMapping("/add_heart_batches")
    @PreAuthorize("@pms.hasPermission('patient_heartRateLogs_add')")
    public R saveInBatches(@RequestBody List<HeartRateLogsEntity> data) {
        return R.ok(heartRateLogsService.saveBatch(data));
    }

    @Operation(summary = "全条件查询心率数据", description = "全条件查询心率数据")
    @PostMapping("/getByHeartRate")
    @PreAuthorize("@pms.hasPermission('patient_heartRateLogs_view')")
    public R getByUserFeedbackObject(@RequestBody HeartRateLogsEntity heartRateLogs) {
        LambdaQueryWrapper<HeartRateLogsEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.setEntity(heartRateLogs);
        return R.ok(heartRateLogsService.list(wrapper));
    }

    @Operation(summary = "查询某一周的所有心率", description = "查询某一周的所有心率")
    @GetMapping("/weekly_heart_rate_data")
    @PreAuthorize("@pms.hasPermission('patient_heartRateLogs_view')")
    public R getWeeklyHeartRateData(@RequestParam int weeksAgo, @RequestParam Long patientUid) {
        JSONArray result = heartRateLogsService.getWeeklyHeartRateData(weeksAgo, patientUid);
        return R.ok(result);
    }

    @Operation(summary = "查询某一个月的所有心率", description = "查询某一个月的所有心率")
    @GetMapping("/monthly_heart_rate_data")
    @PreAuthorize("@pms.hasPermission('patient_heartRateLogs_view')")
    public R getMonthlyHeartRateData(@RequestParam int monthsAgo, @RequestParam Long patientUid) {
        JSONArray result = heartRateLogsService.getMonthlyHeartRateData(monthsAgo, patientUid);
        return R.ok(result);
    }

    @Operation(summary = "查询某一年的所有心率", description = "查询某一年的所有心率")
    @GetMapping("/yearly_heart_rate_data")
    @PreAuthorize("@pms.hasPermission('patient_heartRateLogs_view')")
    public R getYearlyHeartRateData(@RequestParam int yearsAgo, @RequestParam Long patientUid) {
        JSONArray result = heartRateLogsService.getYearlyHeartRateData(yearsAgo, patientUid);
        return R.ok(result);
    }

}