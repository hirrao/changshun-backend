package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * GPT对话表
 *
 * @author wangwenche
 * @date 2024-07-05 16:50:36
 */
@Data
@TableName("gpt_message")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "GPT对话表")
public class GptMessageEntity extends Model<GptMessageEntity> {


	/**
	* 消息唯一标识符（主键）
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="消息唯一标识符（主键）")
    private Long messageId;

	/**
	* 所属患者唯一id
	*/
    @Schema(description="所属患者唯一id")
    private Long patientUid;

	/**
	* 发送者的类型，'USER' 代表用户，'GPT' 代表GPT
	*/
    @Schema(description="发送者的类型，'USER' 代表用户，'GPT' 代表GPT")
    private String senderType;

	/**
	* 消息内容
	*/
    @Schema(description="消息内容")
    private String content;

	/**
	* 消息发送时间
	*/
    @Schema(description="消息发送时间")
    private LocalDateTime sentTime;
}