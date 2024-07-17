package com.pig4cloud.pig.patient.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * <h3>patient</h3>
 *
 * @author HuangJiayu
 * @description <p>异常血压记录实体</p>
 * @date 2024-07-17 20:54
 **/
@Data
public class AbnormalBloodDTO {
	
	private Long patientUid;
	
	// 记录日期
	private LocalDateTime uploadTime;
	// 病情
	private String sdhClassification;
	private Float systolic;
	private Float diastolic;
	private Long sdhId;
	private Integer heartRate;
	
	// 风险评估
	private String riskLevel;
	
}
