package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.entity.PatientCaseEntity;
import com.pig4cloud.pig.patient.service.PatientCaseService;
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
import java.util.Objects;

/**
 * 患者病历
 *
 * @author huangqing
 * @date 2024-07-07 14:53:34
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/patientCase" )
@Tag(description = "patientCase" , name = "患者病历管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatientCaseController {

    private final  PatientCaseService patientCaseService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param patientCase 患者病历
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('patient_patientCase_view')" )
    public R getPatientCasePage(@ParameterObject Page page, @ParameterObject PatientCaseEntity patientCase) {
        LambdaQueryWrapper<PatientCaseEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(patientCaseService.page(page, wrapper));
    }


    /**
     * 通过id查询患者病历
     * @param caseId id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{caseId}" )
    @PreAuthorize("@pms.hasPermission('patient_patientCase_view')" )
    public R getById(@PathVariable("caseId" ) Integer caseId) {
        return R.ok(patientCaseService.getById(caseId));
    }

    /**
     * 新增患者病历
     * @param patientCase 患者病历
     * @return R
     */
    @Operation(summary = "新增患者病历" , description = "新增患者病历" )
    @SysLog("新增患者病历" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('patient_patientCase_add')" )
    public R save(@RequestBody PatientCaseEntity patientCase) {
        return R.ok(patientCaseService.save(patientCase));
    }

    /**
     * 修改患者病历
     * @param patientCase 患者病历
     * @return R
     */
    @Operation(summary = "修改患者病历" , description = "修改患者病历" )
    @SysLog("修改患者病历" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('patient_patientCase_edit')" )
    public R updateById(@RequestBody PatientCaseEntity patientCase) {
        return R.ok(patientCaseService.updateById(patientCase));
    }

    /**
     * 通过id删除患者病历
     * @param ids caseId列表
     * @return R
     */
    @Operation(summary = "通过id删除患者病历" , description = "通过id删除患者病历" )
    @SysLog("通过id删除患者病历" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('patient_patientCase_del')" )
    public R removeById(@RequestBody Integer[] ids) {
        return R.ok(patientCaseService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param patientCase 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('patient_patientCase_export')" )
    public List<PatientCaseEntity> export(PatientCaseEntity patientCase,Integer[] ids) {
        return patientCaseService.list(Wrappers.lambdaQuery(patientCase).in(ArrayUtil.isNotEmpty(ids), PatientCaseEntity::getCaseId, ids));
    }
}