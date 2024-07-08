package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * 血压异常次数统计
 *
 * @author wangwenche
 * @date 2024-07-08 11:20:07
 */
@Data
@TableName("pressure_anomaly")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "血压异常次数统计")
public class PressureAnomalyEntity extends Model<PressureAnomalyEntity> {

 
	/**
	* paId
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="paId")
    private Long paId;
 
	/**
	* patientUid
	*/
    @Schema(description="patientUid")
    private Long patientUid;
 
	/**
	* date
	*/
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
	* low
	*/
    @Schema(description="low")
    private Integer low;
 
	/**
	* allNum
	*/
    @Schema(description="allNum")
    private Integer allNum;
}