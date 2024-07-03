package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 患者基本信息
 *
 * @author huangjiayu
 * @date 2024-07-03 16:07:48
 */
@Data
@TableName("patient_base")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "患者基本信息")
public class PatientBaseEntity extends Model<PatientBaseEntity> {


	/**
	* 患者唯一id
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="患者唯一id")
    private Long patientUid;

	/**
	* 身份证号码
	*/
    @Schema(description="身份证号码")
    private String identificationNumber;
 
	/**
	* patientName
	*/
    @Schema(description="patientName")
    private String patientName;
 
	/**
	* sex
	*/
    @Schema(description="sex")
    private String sex;
 
	/**
	* age
	*/
    @Schema(description="age")
    private Integer age;
 
	/**
	* phoneNumber
	*/
    @Schema(description="phoneNumber")
    private String phoneNumber;

	/**
	* 微信id，确认绑定的微信,一般是openid或者unionid具体选择哪个再确认
	*/
    @Schema(description="微信id，确认绑定的微信,一般是openid或者unionid具体选择哪个再确认")
    private String wxUid;
}