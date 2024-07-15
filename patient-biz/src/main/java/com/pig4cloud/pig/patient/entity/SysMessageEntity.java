package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 系统消息表
 *
 * @author 袁钰涛
 * @date 2024-07-05 23:52:47
 */
@Data
@TableName("sys_message")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统消息表")
public class SysMessageEntity extends Model<SysMessageEntity> {


	/**
	* 通知唯一标识符
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="通知唯一标识符")
    private Long notificationId;
 
	/**
	* doctorUid
	*/
    @Schema(description="doctorUid")
    private Long doctorUid;
 
	/**
	* patientUid
	*/
    @Schema(description="patientUid")
    private Long patientUid;

	/**
	* 消息类型：用药提醒、医生提醒
	*/
    @Schema(description="消息类型：用药提醒、医生提醒")
    private String messageType;

	/**
	* 存储JSON字符串
	*/
    @Schema(description="存储JSON字符串")
    private String jsonText;

	/**
	* 通知发送日期和时间
	*/
    @Schema(description="通知发送日期和时间")
    private LocalDateTime sentDate;

	@Schema(description = "是否已读")
	private Boolean isRead = false; // 新增字段，用于标记是否已读
}