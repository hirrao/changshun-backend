package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * 患者当前疾病信息表
 *
 * @author wangwenche
 * @date 2024-07-03 23:06:46
 */
@Data
@TableName("patient_now_disease")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "患者当前疾病信息表")
public class PatientNowDiseaseEntity extends Model<PatientNowDiseaseEntity> {


	/**
	* 表唯一id，主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="表唯一id，主键")
    private Long pddId;
 
	/**
	* patientUid
	*/
    @Schema(description="patientUid")
    private Long patientUid;

	/**
	* 疾病名称
	*/
    @Schema(description="疾病名称")
    private String disease;

	/**
	* 确诊日期 YYYY-mm-dd
	*/
    @Schema(description="确诊日期 YYYY-mm-dd")
    private LocalDate diseaseTime;
}