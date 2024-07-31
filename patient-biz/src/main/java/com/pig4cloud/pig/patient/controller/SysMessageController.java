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
import com.pig4cloud.pig.patient.entity.SysMessageEntity;
import com.pig4cloud.pig.patient.service.EatDrugAlertService;
import com.pig4cloud.pig.patient.service.SysMessageService;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 系统消息表
 *
 * @author 袁钰涛
 * @date 2024-07-05 23:52:47
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sysMessage" )
@Tag(description = "sysMessage" , name = "系统消息表管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysMessageController {

    private final  SysMessageService sysMessageService;

	@Autowired
	private EatDrugAlertService eatDrugAlertService;


    @GetMapping("/unread/{patientUid}")
    public List<SysMessageEntity> getUnreadMessages(@PathVariable Long patientUid) {
        return sysMessageService.getUnreadMessages(patientUid);
    }

    @PostMapping("/read/{notificationId}")
    public void markMessageAsRead(@PathVariable Long notificationId) {
        sysMessageService.markMessageAsRead(notificationId);
    }
	
	// TODO: 测试检测发提醒信息
	@GetMapping("/test_job")
	public R testJob() {
		eatDrugAlertService.sendDrugAlerts();
		return R.ok();
	}
	
	
    @Operation(summary = "医生发送消息", description = "医生发送消息")
    @PostMapping("/send")
    @PreAuthorize("@pms.hasPermission('patient_send_view')")
    public R sendMessage(@RequestBody JSONObject jsonObject) {

        String message = jsonObject.getJSONObject("message").toJSONString();
        
        Long doctorUid = jsonObject.getLong("doctorUid");
        Long patientUid = jsonObject.getLong("patientUid");
        boolean success = sysMessageService.sendMessage(doctorUid, patientUid, message);
        if (success) {
            return R.ok("消息发送成功");
        } else {
            return R.failed("消息发送失败");
        }
    }

    @Operation(summary = "最近一周已发通知相关信息查询", description = "最近一周已发通知相关信息查询")
    @GetMapping("/recentMsg")
    @PreAuthorize("@pms.hasPermission('patient_sysMessage_view')")
    public R getRecentMessages(@RequestParam Long doctorId) {
        JSONArray messages = sysMessageService.getRecentMessageByDoctorId(doctorId);
        return R.ok(messages);
    }

    @Operation(summary = "查询系统消息", description = "查询系统消息")
    @PostMapping("/getSysMessageById")
    @PreAuthorize("@pms.hasPermission('patient_sysMessage_view')")
    public R getByUserFeedbackObject(@RequestBody SysMessageEntity userSysMessage) {
        LambdaQueryWrapper<SysMessageEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.setEntity(userSysMessage);
        return R.ok(sysMessageService.list(wrapper));
    }

    /**
     * 分页查询
     * @param page 分页对象
     * @param sysMessage 系统消息表
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('patient_sysMessage_view')" )
    public R getSysMessagePage(@ParameterObject Page page, @ParameterObject SysMessageEntity sysMessage) {
        LambdaQueryWrapper<SysMessageEntity> wrapper = Wrappers.lambdaQuery();
        return R.ok(sysMessageService.page(page, wrapper));
    }


    /**
     * 通过id查询系统消息表
     * @param notificationId id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{notificationId}" )
    @PreAuthorize("@pms.hasPermission('patient_sysMessage_view')" )
    public R getById(@PathVariable("notificationId" ) Long notificationId) {
        return R.ok(sysMessageService.getById(notificationId));
    }

    /**
     * 新增系统消息表
     * @param sysMessage 系统消息表
     * @return R
     */
    @Operation(summary = "新增系统消息表" , description = "新增系统消息表" )
    @SysLog("新增系统消息表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('patient_sysMessage_add')" )
    public R save(@RequestBody SysMessageEntity sysMessage) {
        return R.ok(sysMessageService.save(sysMessage));
    }

    /**
     * 修改系统消息表
     * @param sysMessage 系统消息表
     * @return R
     */
    @Operation(summary = "修改系统消息表" , description = "修改系统消息表" )
    @SysLog("修改系统消息表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('patient_sysMessage_edit')" )
    public R updateById(@RequestBody SysMessageEntity sysMessage) {
        return R.ok(sysMessageService.updateById(sysMessage));
    }

    /**
     * 通过id删除系统消息表
     * @param ids notificationId列表
     * @return R
     */
    @Operation(summary = "通过id删除系统消息表" , description = "通过id删除系统消息表" )
    @SysLog("通过id删除系统消息表" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('patient_sysMessage_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(sysMessageService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param sysMessage 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('patient_sysMessage_export')" )
    public List<SysMessageEntity> export(SysMessageEntity sysMessage,Long[] ids) {
        return sysMessageService.list(Wrappers.lambdaQuery(sysMessage).in(ArrayUtil.isNotEmpty(ids), SysMessageEntity::getNotificationId, ids));
    }

    @Operation(summary = "批量添加信息", description = "批量添加信息")
    @PostMapping("/saveBatch")
    @PreAuthorize("@pms.hasPermission('patient_sysMessage_view')")
    public boolean saveBatch(@RequestBody List<SysMessageEntity> entityList) {
        return sysMessageService.saveBatch(entityList);
    }
}