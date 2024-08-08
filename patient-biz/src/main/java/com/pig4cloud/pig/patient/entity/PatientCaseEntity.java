package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * 患者病历
 *
 * @author huangqing
 * @date 2024-07-07 14:53:34
 */
@Data
@TableName("patient_case")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "患者病历")
public class PatientCaseEntity extends Model<PatientCaseEntity> {


	/**
	* 主键，病历id
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="主键，病历id")
    private Long caseId;

	/**
	* 患者唯一id，外键
	*/
    @Schema(description="患者唯一id，外键")
    private Long patientUid;

	/**
	* 病人类型，是否为居民
	*/
    @Schema(description="病人类型，是否为居民")
    private String patientType;

	/**
	* 如：小学、初中等
	*/
    @Schema(description="如：小学、初中等")
    private String educationLevel;

	/**
	* 医疗保险类型
	*/
    @Schema(description="医疗保险类型")
    private String insuranceType;

	/**
	* 省份名称（自治区、直辖市）
	*/
    @Schema(description="省份名称（自治区、直辖市）")
    private String addressProvince;

	/**
	* 城市/地区名称（州）
	*/
    @Schema(description="城市/地区名称（州）")
    private String addressCity;

	/**
	* 县区名称
	*/
    @Schema(description="县区名称")
    private String addressCounty;

	/**
	* 乡镇名称（街道办事处）
	*/
    @Schema(description="乡镇名称（街道办事处）")
    private String addressTown;

	/**
	* 村庄/街道名称（路等）
	*/
    @Schema(description="村庄/街道名称（路等）")
    private String addressVillage;

	/**
	* 详细住址
	*/
    @Schema(description="详细住址")
    private String addressDetail;

	/**
	* 患者职业
	*/
    @Schema(description="患者职业")
    private String occupation;

	/**
	* 发病具体时间
	*/
    @Schema(description="发病具体时间")
    private LocalDate onsetDatetime;

	/**
	* 患者类型名称（1门诊、2急诊、3住院、4体检、9其他）
	*/
    @Schema(description="患者类型名称（1门诊、2急诊、3住院、4体检、9其他）")
    private String patientSource;

	/**
	* 体温值(℃)
	*/
    @Schema(description="体温值(℃)")
    private Float temperature;

	/**
	* 脉率值（次/min）
	*/
    @Schema(description="脉率值（次/min）")
    private Integer pulseRate;

	/**
	* 舒张压值（mmHg）
	*/
    @Schema(description="舒张压值（mmHg）")
    private Float diastolicBp;

	/**
	* 收缩压值（mmHg）
	*/
    @Schema(description="收缩压值（mmHg）")
    private Float systolicBp;

	/**
	* 体重值(kg)
	*/
    @Schema(description="体重值(kg)")
    private Float weight;

	/**
	* 病情代码（1 危 2 急 3 一般 4 重 5 教学 6 科研）
	*/
    @Schema(description="病情代码（1 危 2 急 3 一般 4 重 5 教学 6 科研）")
    private String illnessSeverity;

	/**
	* 患者主诉内容
	*/
    @Schema(description="患者主诉内容")
    private String chiefComplaint;

	/**
	* 现病史
	*/
    @Schema(description="现病史")
    private String presentIllness;

	/**
	* 既往病情记录（既往病史）
	*/
    @Schema(description="既往病情记录（既往病史）")
    private String pastIllness;

	/**
	* 过敏症状名称
	*/
    @Schema(description="过敏症状名称")
    private String allergySymptom;

	/**
	* 过敏源名称
	*/
    @Schema(description="过敏源名称")
    private String allergenName;

	/**
	* 过敏类别名称（ 如0药物过敏）
	*/
    @Schema(description="过敏类别名称（ 如0药物过敏）")
    private String allergyType;

	/**
	* 严重程度名称，例: 0一般
	*/
    @Schema(description="严重程度名称，例: 0一般")
    private String severityLevel;

	/**
	* 体格检查
	*/
    @Schema(description="体格检查")
    private String physicalExam;

	/**
	* 辅助检查结果
	*/
    @Schema(description="辅助检查结果")
    private String auxiliaryExam;

	/**
	* 诊断名称（可多个，逗号分隔或使用关联表）
	*/
    @Schema(description="诊断名称（可多个，逗号分隔或使用关联表）")
    private String diagnosisName;

	/**
	* 其他医学处置
	*/
    @Schema(description="其他医学处置")
    private String otherTreatment;

	/**
	* 医嘱内容
	*/
    @Schema(description="医嘱内容")
    private String doctorAdvice;
}