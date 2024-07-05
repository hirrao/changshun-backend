package com.pig4cloud.pig.pig.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 *
 * @author pig
 * @date 2024-07-04 20:46:16
 */
@Data
@TableName("patient_device")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "")
public class PatientDeviceEntity extends Model<PatientDeviceEntity> {


	/**
	* 表唯一id
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="表唯一id")
    private Long pddId;
 
	/**
	* deviceUid
	*/
    @Schema(description="deviceUid")
    private Long deviceUid;

	/**
	* 绑定设备名称
	*/
    @Schema(description="绑定设备名称")
    private String deviceName;
 
	/**
	* patientUid
	*/
    @Schema(description="patientUid")
    private Long patientUid;
}