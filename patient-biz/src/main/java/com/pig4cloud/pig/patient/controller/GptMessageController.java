package com.pig4cloud.pig.patient.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.patient.entity.GptMessageEntity;
import com.pig4cloud.pig.patient.service.GptMessageService;
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
 * GPT对话表
 *
 * @author wangwenche
 * @date 2024-07-05 16:50:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/gptMessage" )
@Tag(description = "gptMessage" , name = "GPT对话表管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GptMessageController {

    private final  GptMessageService gptMessageService;

    @Operation(summary = "全条件查询百科GPT中的消息", description = "全条件查询百科GPT中的消息")
    @PostMapping("/getGPTMsg")
    @PreAuthorize("@pms.hasPermission('patient_gptMessage_view')")
    public R getByUserFeedbackObject(@RequestBody GptMessageEntity gptMessage) {
        LambdaQueryWrapper<GptMessageEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.setEntity(gptMessage);
        return R.ok(gptMessageService.list(wrapper));
    }

    /**
     * 分页查询
     * @param page 分页对象
     * @param gptMessage GPT对话表
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('patient_gptMessage_view')" )
    public R getGptMessagePage(@ParameterObject Page page, @ParameterObject GptMessageEntity gptMessage) {
        LambdaQueryWrapper<GptMessageEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(gptMessageService.page(page, wrapper));
    }


    /**
     * 通过id查询GPT对话表
     * @param messageId id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{messageId}" )
    @PreAuthorize("@pms.hasPermission('patient_gptMessage_view')" )
    public R getById(@PathVariable("messageId" ) Long messageId) {
        return R.ok(gptMessageService.getById(messageId));
    }

    // 自定义
    /**
     * 通过patient_uid查询所有消息
     * @param patientUid patient_uid
     * @return 所有相关消息
     */
    @Operation(summary = "通过patient_uid查询所有消息", description = "通过patient_uid查询所有消息")
    @GetMapping("/patient/{patientUid}")
    @PreAuthorize("@pms.hasPermission('patient_gptMessage_view')")
    public R getMessagesByPatientUid(@PathVariable("patientUid") Long patientUid) {
        List<GptMessageEntity> messages = gptMessageService.getMessagesByPatientUid(patientUid);
        return R.ok(messages);
    }

    // 自定义
    /**
     * 删除单条信息
     * @param messageId 消息Id
     * @return 删除结果
     */
    @Operation(summary = "删除单条信息", description = "删除单条信息")
    @DeleteMapping("/{messageId}")
    @PreAuthorize("@pms.hasPermission('patient_gptMessage_del')")
    public R deleteMessageById(@PathVariable("messageId") Long messageId){
        boolean result = gptMessageService.deleteMessageById(messageId);
        return result ? R.ok() : R.failed("删除失败");
    }

    // 自定义
    /**
     * 删除用户在百科GPT中的所有消息
     * @param patientUid patient_uid
     * @return 删除结果
     */
    @Operation(summary = "删除用户在百科GPT中的所有消息", description = "删除用户在百科GPT中的所有消息")
    @DeleteMapping("/patient/{patientUid}")
    @PreAuthorize("@pms.hasPermission('patient_gptMessage_del')")
    public R deleteAllMessagesByPatientUid(@PathVariable("patientUid") Long patientUid){
        boolean result = gptMessageService.deleteAllMessagesByPatientUid(patientUid);
        return result ? R.ok() : R.failed("删除失败");
    }

    @Operation(summary = "保存问答记录", description = "保存问答记录")
    @PostMapping("/ask")
    @PreAuthorize("@pms.hasPermission('patient_gptMessage_view')")
    public R askQuestion(@RequestParam long patientUid, @RequestParam String question){
        gptMessageService.saveMessage(patientUid, "USER", question);

        String answer = "这是测试字符串";

        gptMessageService.saveMessage(patientUid, "GPT", answer);
        return R.ok();
    }

    @Operation(summary = "处理点击您可能想问的问题的逻辑", description = "处理点击您可能想问的问题的逻辑")
    @GetMapping("/common-question/{qaId}")
    @PreAuthorize("@pms.hasPermission('patient_gptMessage_add') && @pms.hasPermission('patient_commonQuestion_view')")
    public R handleCommonQuestionClick(
            @PathVariable long qaId,
            @RequestParam long patientUid) {
        JSONObject response = gptMessageService.handleCommonQuestionClick(qaId, patientUid);
        return R.ok(response);
    }

    /**
     * 新增GPT对话表
     * @param gptMessage GPT对话表
     * @return R
     */
    @Operation(summary = "新增GPT对话表" , description = "新增GPT对话表" )
    @SysLog("新增GPT对话表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('patient_gptMessage_add')" )
    public R save(@RequestBody GptMessageEntity gptMessage) {
        return R.ok(gptMessageService.save(gptMessage));
    }

    /**
     * 修改GPT对话表
     * @param gptMessage GPT对话表
     * @return R
     */
    @Operation(summary = "修改GPT对话表" , description = "修改GPT对话表" )
    @SysLog("修改GPT对话表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('patient_gptMessage_edit')" )
    public R updateById(@RequestBody GptMessageEntity gptMessage) {
        return R.ok(gptMessageService.updateById(gptMessage));
    }

    /**
     * 通过id删除GPT对话表
     * @param ids messageId列表
     * @return R
     */
    @Operation(summary = "通过id删除GPT对话表" , description = "通过id删除GPT对话表" )
    @SysLog("通过id删除GPT对话表" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('patient_gptMessage_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(gptMessageService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param gptMessage 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('patient_gptMessage_export')" )
    public List<GptMessageEntity> export(GptMessageEntity gptMessage,Long[] ids) {
        return gptMessageService.list(Wrappers.lambdaQuery(gptMessage).in(ArrayUtil.isNotEmpty(ids), GptMessageEntity::getMessageId, ids));
    }
}