package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * 患者身高体重
 *
 * @author huangqing
 * @date 2024-07-05 01:15:51
 */
@Data
@TableName("patient_bmi_mana")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "患者身高体重")
public class PatientBmiManaEntity extends Model<PatientBmiManaEntity> {

 
	/**
	* bmiUuid
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="bmiUuid")
    private Long bmiUuid;
 
	/**
	* patientUid
	*/
    @Schema(description="patientUid")
    private Long patientUid;

	/**
	* 体重（单位：千克）
	*/
    @Schema(description="体重（单位：千克）")
    private Float weight;

	/**
	* 测量日期
	*/
    @Schema(description="测量日期")
    private LocalDate bmimeasurementDate;

	/**
	* 身高（单位：厘米）
	*/
    @Schema(description="身高（单位：厘米）")
    private Float height;
}