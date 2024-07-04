package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.entity.EatDrugAlertEntity;
import com.pig4cloud.pig.patient.service.EatDrugAlertService;
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
 * 用药管理表
 *
 * @author huangqing
 * @date 2024-07-05 01:50:45
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/eatDrugAlert" )
@Tag(description = "eatDrugAlert" , name = "用药管理表管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class EatDrugAlertController {

    private final  EatDrugAlertService eatDrugAlertService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param eatDrugAlert 用药管理表
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('patient_eatDrugAlert_view')" )
    public R getEatDrugAlertPage(@ParameterObject Page page, @ParameterObject EatDrugAlertEntity eatDrugAlert) {
        LambdaQueryWrapper<EatDrugAlertEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(eatDrugAlertService.page(page, wrapper));
    }


    /**
     * 通过id查询用药管理表
     * @param pdeId id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{pdeId}" )
    @PreAuthorize("@pms.hasPermission('patient_eatDrugAlert_view')" )
    public R getById(@PathVariable("pdeId" ) Long pdeId) {
        return R.ok(eatDrugAlertService.getById(pdeId));
    }

    /**
     * 新增用药管理表
     * @param eatDrugAlert 用药管理表
     * @return R
     */
    @Operation(summary = "新增用药管理表" , description = "新增用药管理表" )
    @SysLog("新增用药管理表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('patient_eatDrugAlert_add')" )
    public R save(@RequestBody EatDrugAlertEntity eatDrugAlert) {
        return R.ok(eatDrugAlertService.save(eatDrugAlert));
    }

    /**
     * 修改用药管理表
     * @param eatDrugAlert 用药管理表
     * @return R
     */
    @Operation(summary = "修改用药管理表" , description = "修改用药管理表" )
    @SysLog("修改用药管理表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('patient_eatDrugAlert_edit')" )
    public R updateById(@RequestBody EatDrugAlertEntity eatDrugAlert) {
        return R.ok(eatDrugAlertService.updateById(eatDrugAlert));
    }

    /**
     * 通过id删除用药管理表
     * @param ids pdeId列表
     * @return R
     */
    @Operation(summary = "通过id删除用药管理表" , description = "通过id删除用药管理表" )
    @SysLog("通过id删除用药管理表" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('patient_eatDrugAlert_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(eatDrugAlertService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param eatDrugAlert 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('patient_eatDrugAlert_export')" )
    public List<EatDrugAlertEntity> export(EatDrugAlertEntity eatDrugAlert,Long[] ids) {
        return eatDrugAlertService.list(Wrappers.lambdaQuery(eatDrugAlert).in(ArrayUtil.isNotEmpty(ids), EatDrugAlertEntity::getPdeId, ids));
    }
}