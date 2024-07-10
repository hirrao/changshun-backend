package com.pig4cloud.pig.patient.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 血压心率展示
 *
 * @author wangwenche
 * @date 2024-07-04 20:02:22
 */
@Data
@TableName("persure_heart_rate")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "血压心率展示")
public class PersureHeartRateEntity extends Model<PersureHeartRateEntity> {


	/**
	* 表唯一id，主键
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="表唯一id，主键")
    private Long sdhId;

	/**
	* 收缩压，高压（单位：mmHg）
	*/
    @Schema(description="收缩压，高压（单位：mmHg）")
    private Float systolic;

	/**
	* 舒张压，低压（单位：mmHg）
	*/
    @Schema(description="舒张压，低压（单位：mmHg）")
    private Float diastolic;
 
	/**
	* heartRate
	*/
    @Schema(description="heartRate")
    private Integer heartRate;

	/**
	* 记录日期保存到时间级别 YYYY-mm-dd HH:ii:ss
	*/
    @Schema(description="记录日期保存到时间级别 YYYY-mm-dd HH:ii:ss")
    private LocalDateTime uploadTime;

	/**
	* 患者唯一id
	*/
    @Schema(description="患者唯一id")
    private Long patientUid;

	/**
	 * 自动生成，具体分为：一级高血压低危，一级高血压中危，一级高血压高危，二级高血压中危，二级高血压高危，三级高血压高危
	 */
	@Schema(description="自动生成，具体分为：一级高血压低危，一级高血压中危，一级高血压高危，二级高血压中危，二级高血压高危，三级高血压高危")
	private String sdhClassification;
}