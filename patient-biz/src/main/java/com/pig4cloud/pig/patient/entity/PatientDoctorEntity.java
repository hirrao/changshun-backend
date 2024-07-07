package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 医患绑定表
 *
 * @author 袁钰涛
 * @date 2024-07-05 10:56:25
 */
@Data
@TableName("patient_doctor")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "医患绑定表")
public class PatientDoctorEntity extends Model<PatientDoctorEntity> {

 
	/**
	* pdId
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="pdId")
    private Long pdId;
 
	/**
	* patientUid
	*/
    @Schema(description="patientUid")
    private Long patientUid;
 
	/**
	* doctorUid
	*/
    @Schema(description="doctorUid")
    private Long doctorUid;

	/**
	 * care
	 */
	@Schema(description="care")
	private Integer care;
}