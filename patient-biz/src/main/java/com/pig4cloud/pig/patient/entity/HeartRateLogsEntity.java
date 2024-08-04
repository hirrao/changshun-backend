package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 患者心率数据
 *
 * @author wangwenche
 * @date 2024-08-04 11:45:38
 */
@Data
@TableName("heart_rate_logs")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "患者心率数据")
public class HeartRateLogsEntity extends Model<HeartRateLogsEntity> {

 
	/**
	* hrlId
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="hrlId")
    private Long hrlId;
 
	/**
	* heartRate
	*/
    @Schema(description="heartRate")
    private Integer heartRate;
 
	/**
	* uploadTime
	*/
    @Schema(description="uploadTime")
    private LocalDateTime uploadTime;
 
	/**
	* patientUid
	*/
    @Schema(description="patientUid")
    private Long patientUid;
}