package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.dto.DiseasesCountDTO;
import com.pig4cloud.pig.patient.entity.AiPreDiagnosisEntity;
import com.pig4cloud.pig.patient.service.AiPreDiagnosisService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * AI预问诊
 *
 * @author wangwenche
 * @date 2024-07-07 11:55:10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/aiPreDiagnosis" )
@Tag(description = "aiPreDiagnosis" , name = "AI预问诊管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AiPreDiagnosisController {

    private final  AiPreDiagnosisService aiPreDiagnosisService;

    @Operation(summary = "得到特别关心客观病史", description = "得到特别关心客观病史")
    @GetMapping("/count-cpatients-history/{doctorUid}")
    @PreAuthorize("@pms.hasPermission('patient_aiPreDiagnosis_hisccount')")
    public ResponseEntity<Map<String, Integer>> ccountPatientsHistory(@PathVariable Long doctorUid) {
        Map<String, Integer> historyCounts = new HashMap<>();

        // 获取统计结果
        int hypertensionFamilyHistoryCount = aiPreDiagnosisService.ccountPatientsWithHypertensionFamilyHistory(doctorUid);
        int smokingHistoryCount = aiPreDiagnosisService.ccountPatientsWithSmokingHistory(doctorUid);
        int drinkingHistoryCount = aiPreDiagnosisService.ccountPatientsWithDrinkingHistory(doctorUid);
        int infectiousHistoryCount = aiPreDiagnosisService.ccountPatientsWithInfectiousHistory(doctorUid);
        int foodAllergyHistoryCount = aiPreDiagnosisService.ccountPatientsWithFoodAllergyHistory(doctorUid);

        // 放入结果到Map中
        historyCounts.put("hypertensionFamilyHistory", hypertensionFamilyHistoryCount);
        historyCounts.put("smokingHistory", smokingHistoryCount);
        historyCounts.put("drinkingHistory", drinkingHistoryCount);
        historyCounts.put("infectiousHistory", infectiousHistoryCount);
        historyCounts.put("foodAllergyHistory", foodAllergyHistoryCount);

        return ResponseEntity.ok(historyCounts);
    }

    @Operation(summary = "得到客观病史", description = "得到客观病史")
    @GetMapping("/count-patients-history/{doctorUid}")
    @PreAuthorize("@pms.hasPermission('patient_aiPreDiagnosis_hiscount')")
    public ResponseEntity<Map<String, Integer>> countPatientsHistory(@PathVariable Long doctorUid) {
        Map<String, Integer> historyCounts = new HashMap<>();

        // 获取统计结果
        int hypertensionFamilyHistoryCount = aiPreDiagnosisService.countPatientsWithHypertensionFamilyHistory(doctorUid);
        int smokingHistoryCount = aiPreDiagnosisService.countPatientsWithSmokingHistory(doctorUid);
        int drinkingHistoryCount = aiPreDiagnosisService.countPatientsWithDrinkingHistory(doctorUid);
        int infectiousHistoryCount = aiPreDiagnosisService.countPatientsWithInfectiousHistory(doctorUid);
        int foodAllergyHistoryCount = aiPreDiagnosisService.countPatientsWithFoodAllergyHistory(doctorUid);

        // 放入结果到Map中
        historyCounts.put("hypertensionFamilyHistory", hypertensionFamilyHistoryCount);
        historyCounts.put("smokingHistory", smokingHistoryCount);
        historyCounts.put("drinkingHistory", drinkingHistoryCount);
        historyCounts.put("infectiousHistory", infectiousHistoryCount);
        historyCounts.put("foodAllergyHistory", foodAllergyHistoryCount);

        return ResponseEntity.ok(historyCounts);
    }

    @Operation(summary = "得到特别关心伴随疾病", description = "得到特别关心伴随疾病")
    @GetMapping("/count-patients/{doctorUid}")
    @PreAuthorize("@pms.hasPermission('patient_aiPreDiagnosis_carecount')")
    public R<Integer> countPatientsWithDiseases(@PathVariable Long doctorUid) {
        int count = aiPreDiagnosisService.countPatientsWithDiseases(doctorUid);
        return R.ok(count);
    }


    @Operation(summary = "得到伴随疾病", description = "得到伴随疾病")
    @GetMapping("/device-diseases-count")
    @PreAuthorize("@pms.hasPermission('patient_aiPreDiagnosis_count')")
    public R<Integer> nocountPatientsWithDiseases(@PathVariable Long doctorUid) {
        int count = aiPreDiagnosisService.nocountPatientsWithDiseases(doctorUid);
        return R.ok(count);
    }

    @Operation(summary = "全条件查询AI预问诊信息", description = "全条件查询AI预问诊信息")
    @PostMapping("/getAiPreDiagnosisMsg")
    @PreAuthorize("@pms.hasPermission('patient_aiPreDiagnosis_view')")
    public R getByUserFeedbackObject(@RequestBody AiPreDiagnosisEntity aiPreDiagnosis) {
        LambdaQueryWrapper<AiPreDiagnosisEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.setEntity(aiPreDiagnosis);
        return R.ok(aiPreDiagnosisService.list(wrapper));
    }


    /**
     * 分页查询
     * @param page 分页对象
     * @param aiPreDiagnosis AI预问诊
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('patient_aiPreDiagnosis_view')" )
    public R getAiPreDiagnosisPage(@ParameterObject Page page, @ParameterObject AiPreDiagnosisEntity aiPreDiagnosis) {
        LambdaQueryWrapper<AiPreDiagnosisEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(aiPreDiagnosisService.page(page, wrapper));
    }


    /**
     * 通过id查询AI预问诊
     * @param aiId id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{aiId}" )
    @PreAuthorize("@pms.hasPermission('patient_aiPreDiagnosis_view')" )
    public R getById(@PathVariable("aiId" ) Long aiId) {
        return R.ok(aiPreDiagnosisService.getById(aiId));
    }

    /**
     * 新增AI预问诊
     * @param aiPreDiagnosis AI预问诊
     * @return R
     */
    @Operation(summary = "新增AI预问诊" , description = "新增AI预问诊" )
    @SysLog("新增AI预问诊" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('patient_aiPreDiagnosis_add')" )
    public R save(@RequestBody AiPreDiagnosisEntity aiPreDiagnosis) {
        return R.ok(aiPreDiagnosisService.saveAiPreDiagnosis(aiPreDiagnosis));
    }

    /**
     * 修改AI预问诊
     * @param aiPreDiagnosis AI预问诊
     * @return R
     */
    @Operation(summary = "修改AI预问诊" , description = "修改AI预问诊" )
    @SysLog("修改AI预问诊" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('patient_aiPreDiagnosis_edit')" )
    public R updateById(@RequestBody AiPreDiagnosisEntity aiPreDiagnosis) {
        return R.ok(aiPreDiagnosisService.updateAiPreDiagnosis(aiPreDiagnosis));
    }

    /**
     * 通过id删除AI预问诊
     * @param ids aiId列表
     * @return R
     */
    @Operation(summary = "通过id删除AI预问诊" , description = "通过id删除AI预问诊" )
    @SysLog("通过id删除AI预问诊" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('patient_aiPreDiagnosis_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(aiPreDiagnosisService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param aiPreDiagnosis 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('patient_aiPreDiagnosis_export')" )
    public List<AiPreDiagnosisEntity> export(AiPreDiagnosisEntity aiPreDiagnosis,Long[] ids) {
        return aiPreDiagnosisService.list(Wrappers.lambdaQuery(aiPreDiagnosis).in(ArrayUtil.isNotEmpty(ids), AiPreDiagnosisEntity::getAiId, ids));
    }
}