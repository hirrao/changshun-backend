package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 *
 * @author wangwenche
 * @date 2024-07-06 20:21:53
 */
@Data
@TableName("common_question")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class CommonQuestionEntity extends Model<CommonQuestionEntity> {


	/**
	* 问题及其对应的答案
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="问题及其对应的答案")
    private Long qaId;

	/**
	* 问题内容
	*/
    @Schema(description="问题内容")
    private String questionText;
 
	/**
	* answerText
	*/
    @Schema(description="answerText")
    private String answerText;
}