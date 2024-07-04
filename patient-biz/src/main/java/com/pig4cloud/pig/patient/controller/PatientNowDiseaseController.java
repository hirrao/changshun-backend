package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.entity.PatientNowDiseaseEntity;
import com.pig4cloud.pig.patient.service.PatientNowDiseaseService;
import io.swagger.v3.oas.annotations.Parameter;
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
 * 患者当前疾病信息表
 *
 * @author wangwenche
 * @date 2024-07-03 23:06:46
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/patientNowDisease" )
@Tag(description = "patientNowDisease" , name = "患者当前疾病信息表管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatientNowDiseaseController {

    private final  PatientNowDiseaseService patientNowDiseaseService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param patientNowDisease 患者当前疾病信息表
     * @return R
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('patient_patientNowDisease_view')" )
    public R getPatientNowDiseasePage(@ParameterObject Page page, @ParameterObject PatientNowDiseaseEntity patientNowDisease) {
        LambdaQueryWrapper<PatientNowDiseaseEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(patientNowDiseaseService.page(page, wrapper));
    }

    @Operation(summary = "全条件查询病人当前疾病信息", description = "全条件查询病人当前疾病信息")
    @PostMapping("/getByPatientNowDiseaseObject")
    @PreAuthorize("@pms.hasPermission('patient_userFeedback_view')")
    public R getByPatientNowDiseaseObject(@RequestBody PatientNowDiseaseEntity patientNowDisease) {
        LambdaQueryWrapper<PatientNowDiseaseEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.setEntity(patientNowDisease);
        return R.ok(patientNowDiseaseService.list(wrapper));
    }

/*
    示例
    @Operation(summary= "条件查询", description = "条件查询")
    @PostMapping("/wrapper") // 前端的请求
    @PreAuthorize("@pms.hasPermission('patient_patientNowDisease_view')" )
    public R getPatientMsg(@Parameter String s){ // 学习各种注解的作用
        String s1 = patientNowDiseaseService.test(s);
        return R.ok(s1);
    }
*/
    /**
     * 通过id查询患者当前疾病信息表
     * @param pddId id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{pddId}" )
    @PreAuthorize("@pms.hasPermission('patient_patientNowDisease_view')" )
    public R getById(@PathVariable("pddId" ) Long pddId) {

        return R.ok(patientNowDiseaseService.getById(pddId));
    }

    /**
     * 新增患者当前疾病信息表
     * @param patientNowDisease 患者当前疾病信息表
     * @return R
     */
    @Operation(summary = "新增患者当前疾病信息表" , description = "新增患者当前疾病信息表" )
    @SysLog("新增患者当前疾病信息表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('patient_patientNowDisease_add')" )
    public R save(@RequestBody PatientNowDiseaseEntity patientNowDisease) {
        return R.ok(patientNowDiseaseService.save(patientNowDisease));
    }

    /**
     * 修改患者当前疾病信息表
     * @param patientNowDisease 患者当前疾病信息表
     * @return R
     */
    @Operation(summary = "修改患者当前疾病信息表" , description = "修改患者当前疾病信息表" )
    @SysLog("修改患者当前疾病信息表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('patient_patientNowDisease_edit')" )
    public R updateById(@RequestBody PatientNowDiseaseEntity patientNowDisease) {
        return R.ok(patientNowDiseaseService.updateById(patientNowDisease));
    }

    /**
     * 通过id删除患者当前疾病信息表
     * @param ids pddId列表
     * @return R
     */
    @Operation(summary = "通过id删除患者当前疾病信息表" , description = "通过id删除患者当前疾病信息表" )
    @SysLog("通过id删除患者当前疾病信息表" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('patient_patientNowDisease_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(patientNowDiseaseService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param patientNowDisease 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('patient_patientNowDisease_export')" )
    public List<PatientNowDiseaseEntity> export(PatientNowDiseaseEntity patientNowDisease,Long[] ids) {
        return patientNowDiseaseService.list(Wrappers.lambdaQuery(patientNowDisease).in(ArrayUtil.isNotEmpty(ids), PatientNowDiseaseEntity::getPddId, ids));
    }


}