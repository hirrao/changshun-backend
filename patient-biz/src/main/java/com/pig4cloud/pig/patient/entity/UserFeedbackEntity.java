package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 用户反馈信息管理
 *
 * @author huangjiayu
 * @date 2024-07-04 15:59:24
 */
@Data
@TableName("user_feedback")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户反馈信息管理")
public class UserFeedbackEntity extends Model<UserFeedbackEntity> {

 
	/**
	* prrId
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="prrId")
    private Long prrId;
 
	/**
	* patientUid
	*/
    @Schema(description="patientUid")
    private Long patientUid;

	/**
	* 反馈标题，限定80字以内
	*/
    @Schema(description="反馈标题，限定80字以内")
    private String remarkTitle;

	/**
	* 反馈内容
	*/
    @Schema(description="反馈内容")
    private String remark;

	/**
	* 填写反馈时间
	*/
    @Schema(description="填写反馈时间")
    private LocalDateTime remarkTime;
}