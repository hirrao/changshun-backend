package com.pig4cloud.pig.doctor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 医生基础信息管理
 *
 * @author huangjiayu
 * @date 2024-07-03 17:17:03
 */
@Data
@TableName("doctor_base")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "医生基础信息管理")
public class DoctorBaseEntity extends Model<DoctorBaseEntity> {
	
	/**
	 * 医生id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "医生id")
	private Long doctorUid;
	
	/**
	 * doctorName
	 */
	@Schema(description = "doctorName")
	private String doctorName;
	
	/**
	 * doctorPhonenumber
	 */
	@Schema(description = "doctorPhonenumber")
	private String doctorPhonenumber;
	
	/**
	 * 医生职务（如主任医师、副主任医师等）
	 */
	@Schema(description = "医生职务（如主任医师、副主任医师等）")
	private String position;
	
	/**
	 * 所属医疗机构名称
	 */
	@Schema(description = "所属医疗机构名称")
	private String affiliatedHospital;
	
	/**
	 * 权限管理：医生、管理员
	 */
	@Schema(description = "权限管理：医生、管理员")
	private String permission;
	
	/**
	 * 所属科室
	 */
	@Schema(description = "所属科室")
	private String department;
	
	/**
	 * 登录系统的用户名，方便websocket使用，也是唯一的
	 */
	@Schema(description = "登录系统的用户名，方便websocket使用，也是唯一的")
	private String username;
}