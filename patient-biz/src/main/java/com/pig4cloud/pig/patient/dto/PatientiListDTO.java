package com.pig4cloud.pig.patient.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * <h3>patient</h3>
 *
 * @author HuangJiayu
 * @description <p>患者列表实体类</p>
 * @date 2024-07-17 16:10
 **/
@Data
public class PatientiListDTO {
	
	// 医生uid
	private Long doctorUid;
	
	// 患者基本信息
	private Long patientUid;
	private String identificationNumber;
	private String patientName;
	private String phoneNumber;
	
	// 高血压相关
	private Float systolic;
	private Float diastolic;
	private Integer heartRate;
	private String sdhClassification;
	private LocalDateTime uploadTime;
	
	//	是否特别关心
	private Integer care;
}
