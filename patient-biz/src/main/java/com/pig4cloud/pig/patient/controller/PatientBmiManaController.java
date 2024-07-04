package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.entity.PatientBmiManaEntity;
import com.pig4cloud.pig.patient.service.PatientBmiManaService;
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
 * 患者身高体重
 *
 * @author huangqing
 * @date 2024-07-05 01:15:51
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/patientBmiMana" )
@Tag(description = "patientBmiMana" , name = "患者身高体重管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatientBmiManaController {

    private final  PatientBmiManaService patientBmiManaService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param patientBmiMana 患者身高体重
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('patient_patientBmiMana_view')" )
    public R getPatientBmiManaPage(@ParameterObject Page page, @ParameterObject PatientBmiManaEntity patientBmiMana) {
        LambdaQueryWrapper<PatientBmiManaEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(patientBmiManaService.page(page, wrapper));
    }


    /**
     * 通过id查询患者身高体重
     * @param bmiUuid id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{bmiUuid}" )
    @PreAuthorize("@pms.hasPermission('patient_patientBmiMana_view')" )
    public R getById(@PathVariable("bmiUuid" ) Long bmiUuid) {
        return R.ok(patientBmiManaService.getById(bmiUuid));
    }

    /**
     * 新增患者身高体重
     * @param patientBmiMana 患者身高体重
     * @return R
     */
    @Operation(summary = "新增患者身高体重" , description = "新增患者身高体重" )
    @SysLog("新增患者身高体重" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('patient_patientBmiMana_add')" )
    public R save(@RequestBody PatientBmiManaEntity patientBmiMana) {
        return R.ok(patientBmiManaService.save(patientBmiMana));
    }

    /**
     * 修改患者身高体重
     * @param patientBmiMana 患者身高体重
     * @return R
     */
    @Operation(summary = "修改患者身高体重" , description = "修改患者身高体重" )
    @SysLog("修改患者身高体重" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('patient_patientBmiMana_edit')" )
    public R updateById(@RequestBody PatientBmiManaEntity patientBmiMana) {
        return R.ok(patientBmiManaService.updateById(patientBmiMana));
    }

    /**
     * 通过id删除患者身高体重
     * @param ids bmiUuid列表
     * @return R
     */
    @Operation(summary = "通过id删除患者身高体重" , description = "通过id删除患者身高体重" )
    @SysLog("通过id删除患者身高体重" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('patient_patientBmiMana_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(patientBmiManaService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param patientBmiMana 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('patient_patientBmiMana_export')" )
    public List<PatientBmiManaEntity> export(PatientBmiManaEntity patientBmiMana,Long[] ids) {
        return patientBmiManaService.list(Wrappers.lambdaQuery(patientBmiMana).in(ArrayUtil.isNotEmpty(ids), PatientBmiManaEntity::getBmiUuid, ids));
    }
}