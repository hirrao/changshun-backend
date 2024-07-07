package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * 血压异常信息统计
 *
 * @author wangwenche
 * @date 2024-07-07 17:44:20
 */
@Data
@TableName("pressure_anomaly_logs")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "血压异常信息统计")
public class PressureAnomalyLogsEntity extends Model<PressureAnomalyLogsEntity> {

 
	/**
	* date
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="date")
    private LocalDate date;
 
	/**
	* severe
	*/
    @Schema(description="severe")
    private Integer severe;
 
	/**
	* moderate
	*/
    @Schema(description="moderate")
    private Integer moderate;
 
	/**
	* mild
	*/
    @Schema(description="mild")
    private Integer mild;
 
	/**
	* elevated
	*/
    @Schema(description="elevated")
    private Integer elevated;
 
	/**
	* allData
	*/
    @Schema(description="allData")
    private Integer allData;

	/**
	 * low
	 */
	@Schema(description = "low")
	private Integer low;
}