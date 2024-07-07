package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * AI预问诊
 *
 * @author wangwenche
 * @date 2024-07-07 11:55:10
 */
@Data
@TableName("ai_pre_diagnosis")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AI预问诊")
public class AiPreDiagnosisEntity extends Model<AiPreDiagnosisEntity> {

 
	/**
	* aiId
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="aiId")
    private Long aiId;
 
	/**
	* patientUid
	*/
    @Schema(description="patientUid")
    private Long patientUid;
 
	/**
	* gender
	*/
    @Schema(description="gender")
    private String gender;
 
	/**
	* birthday
	*/
    @Schema(description="birthday")
    private LocalDate birthday;
 
	/**
	* height
	*/
    @Schema(description="height")
    private Float height;
 
	/**
	* weight
	*/
    @Schema(description="weight")
    private Float weight;
 
	/**
	* bloodPressureHigh
	*/
    @Schema(description="bloodPressureHigh")
    private Float bloodPressureHigh;
 
	/**
	* bloodPressureLow
	*/
    @Schema(description="bloodPressureLow")
    private Float bloodPressureLow;
 
	/**
	* pregnancyHistory
	*/
    @Schema(description="pregnancyHistory")
    private String pregnancyHistory;
 
	/**
	* diseasesList
	*/
    @Schema(description="diseasesList")
    private String diseasesList;
 
	/**
	* kidneyDiseases
	*/
    @Schema(description="kidneyDiseases")
    private String kidneyDiseases;
 
	/**
	* creatinineLevel
	*/
    @Schema(description="creatinineLevel")
    private Float creatinineLevel;
 
	/**
	* heartRate
	*/
    @Schema(description="heartRate")
    private Integer heartRate;
 
	/**
	* bloodDiseases
	*/
    @Schema(description="bloodDiseases")
    private String bloodDiseases;
 
	/**
	* totalCholesterol
	*/
    @Schema(description="totalCholesterol")
    private Float totalCholesterol;
 
	/**
	* beDiseases
	*/
    @Schema(description="beDiseases")
    private String beDiseases;
 
	/**
	* heartDiseases
	*/
    @Schema(description="heartDiseases")
    private String heartDiseases;
 
	/**
	* vesselDiseases
	*/
    @Schema(description="vesselDiseases")
    private String vesselDiseases;
 
	/**
	* earlyCvdFamilyHistory
	*/
    @Schema(description="earlyCvdFamilyHistory")
    private Integer earlyCvdFamilyHistory;
 
	/**
	* geneticDiseaseInFamily
	*/
    @Schema(description="geneticDiseaseInFamily")
    private String geneticDiseaseInFamily;
 
	/**
	* infectiousDiseaseHistory
	*/
    @Schema(description="infectiousDiseaseHistory")
    private String infectiousDiseaseHistory;
 
	/**
	* foodAllergyHistory
	*/
    @Schema(description="foodAllergyHistory")
    private String foodAllergyHistory;
 
	/**
	* smokingStatus
	*/
    @Schema(description="smokingStatus")
    private String smokingStatus;
 
	/**
	* smokingDuration
	*/
    @Schema(description="smokingDuration")
    private Integer smokingDuration;
 
	/**
	* dailySmokingAmount
	*/
    @Schema(description="dailySmokingAmount")
    private Integer dailySmokingAmount;
 
	/**
	* drinkingStatus
	*/
    @Schema(description="drinkingStatus")
    private String drinkingStatus;
 
	/**
	* drinkingDuration
	*/
    @Schema(description="drinkingDuration")
    private Integer drinkingDuration;
 
	/**
	* dailyDrinkingAmount
	*/
    @Schema(description="dailyDrinkingAmount")
    private Float dailyDrinkingAmount;
}