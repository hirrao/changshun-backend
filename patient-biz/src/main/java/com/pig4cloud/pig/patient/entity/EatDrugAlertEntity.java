package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用药管理表
 *
 * @author huangqing
 * @date 2024-07-05 01:50:45
 */
@Data
@TableName("eat_drug_alert")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用药管理表")
public class EatDrugAlertEntity extends Model<EatDrugAlertEntity> {


	/**
	* 表唯一id，主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="表唯一id，主键")
    private Long pdeId;
 
	/**
	* patientUid
	*/
    @Schema(description="patientUid")
    private Long patientUid;
 
	/**
	* drugName
	*/
    @Schema(description="drugName")
    private String drugName;

	/**
	* 用药频率：每天，每隔1天~每隔10天，每周，每2周~每8周
	*/
    @Schema(description="用药频率：每天，每隔1天~每隔10天，每周，每2周~每8周")
    private String frequency;

	/**
	* 单位(毫克，毫升，微克，克，片，支，粒，包，瓶)
	*/
    @Schema(description="单位(毫克，毫升，微克，克，片，支，粒，包，瓶)")
    private String unit;

	/**
	* 服用剂量
	*/
    @Schema(description="服用剂量")
    private Integer dose;

	/**
	* 是否激活该用药计划
	*/
    @Schema(description="是否激活该用药计划")
    private Integer isActive;
}