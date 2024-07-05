package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import java.sql.Time;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * 用药时间对照表（因为一款药可能需要多个时间）
 *
 * @author 袁钰涛
 * @date 2024-07-05 09:57:07
 */
@Data
@TableName("drug_eat_time")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用药时间对照表（因为一款药可能需要多个时间）")
public class DrugEatTimeEntity extends Model<DrugEatTimeEntity> {

 
	/**
	* pepId
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="pepId")
    private Long pepId;
 
	/**
	* patientUid
	*/
    @Schema(description="patientUid")
    private Long patientUid;

	/**
	* 用药时间，24小时制
	*/
    @Schema(description="用药时间，24小时制")
    private Time eatTime;

	/**
	* 用药管理表唯一id，依据此id可知该时间是哪个用药管理元素里面的
	*/
    @Schema(description="用药管理表唯一id，依据此id可知该时间是哪个用药管理元素里面的")
    private Long pdeId;
 
	/**
	* lastEatTime
	*/
    @Schema(description="lastEatTime")
    private LocalDate lastEatTime;
}