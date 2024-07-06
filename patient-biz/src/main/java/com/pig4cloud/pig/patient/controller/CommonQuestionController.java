package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.entity.CommonQuestionEntity;
import com.pig4cloud.pig.patient.service.CommonQuestionService;
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
 * 
 *
 * @author wangwenche
 * @date 2024-07-06 20:21:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/commonQuestion" )
@Tag(description = "commonQuestion" , name = "管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class CommonQuestionController {

    private final  CommonQuestionService commonQuestionService;

    @Operation(summary = "获取随机问题", description = "获取随机的三个问题")
    @GetMapping("/random")
    @PreAuthorize("@pms.hasPermission('patient_commonQuestion_view')")
    public R getRandomQuestions(){
        List<CommonQuestionEntity> questions = commonQuestionService.getRandomQuestions();
        return R.ok(questions);
    }

    @Operation(summary = "全条件查询常见问题和答案", description = "全条件查询常见问题和答案")
    @PostMapping("/getAnswer")
    @PreAuthorize("@pms.hasPermission('patient_commonQuestion_view')")
    public R getByUserFeedbackObject(@RequestBody CommonQuestionEntity commonQuestion) {
        LambdaQueryWrapper<CommonQuestionEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.setEntity(commonQuestion);
        return R.ok(commonQuestionService.list(wrapper));
    }



    /**
     * 分页查询
     * @param page 分页对象
     * @param commonQuestion 
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('patient_commonQuestion_view')" )
    public R getCommonQuestionPage(@ParameterObject Page page, @ParameterObject CommonQuestionEntity commonQuestion) {
        LambdaQueryWrapper<CommonQuestionEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(commonQuestionService.page(page, wrapper));
    }


    /**
     * 通过id查询
     * @param qaId id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{qaId}" )
    @PreAuthorize("@pms.hasPermission('patient_commonQuestion_view')" )
    public R getById(@PathVariable("qaId" ) Long qaId) {
        return R.ok(commonQuestionService.getById(qaId));
    }

    /**
     * 新增
     * @param commonQuestion 
     * @return R
     */
    @Operation(summary = "新增" , description = "新增" )
    @SysLog("新增" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('patient_commonQuestion_add')" )
    public R save(@RequestBody CommonQuestionEntity commonQuestion) {
        return R.ok(commonQuestionService.save(commonQuestion));
    }

    /**
     * 修改
     * @param commonQuestion 
     * @return R
     */
    @Operation(summary = "修改" , description = "修改" )
    @SysLog("修改" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('patient_commonQuestion_edit')" )
    public R updateById(@RequestBody CommonQuestionEntity commonQuestion) {
        return R.ok(commonQuestionService.updateById(commonQuestion));
    }

    /**
     * 通过id删除
     * @param ids qaId列表
     * @return R
     */
    @Operation(summary = "通过id删除" , description = "通过id删除" )
    @SysLog("通过id删除" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('patient_commonQuestion_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(commonQuestionService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param commonQuestion 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('patient_commonQuestion_export')" )
    public List<CommonQuestionEntity> export(CommonQuestionEntity commonQuestion,Long[] ids) {
        return commonQuestionService.list(Wrappers.lambdaQuery(commonQuestion).in(ArrayUtil.isNotEmpty(ids), CommonQuestionEntity::getQaId, ids));
    }
}