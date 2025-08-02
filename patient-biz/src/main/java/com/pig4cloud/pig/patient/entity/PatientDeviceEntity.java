package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 *
 * @author wangwenche
 * @date 2024-07-04 20:47:58
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
    private String deviceUid;

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

    /**
     * 设备品牌, 汉语拼音首字母
     * 比如xy
     */
	@Schema(description = "设备品牌")
	private String deviceBrand;

	/**
	 * 最后一次同步数据时间
	 */
	@Schema(description="最后一次同步数据时间")
	private LocalDateTime lastUpdateTime;
}