package com.pig4cloud.pig.admin.api.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.pig4cloud.plugin.excel.annotation.ExcelLine;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * <h3>changshun-backend</h3>
 *
 * @author HuangJiayu
 * @description <p>自定义批量导入医生账号实体</p>
 * @date 2024-08-14 10:11
 **/
@Data
@ColumnWidth(30)
public class DoctorExcelVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 导入时候回显行号
	 */
	@ExcelLine
	@ExcelIgnore
	private Long lineNum;
	
	/**
	 * 主键ID
	 */
	@ExcelProperty("用户编号")
	private Long userId;
	
	/**
	 * 手机号
	 */
	@NotBlank(message = "手机号不能为空")
	@ExcelProperty("手机号")
	private String phone;
	
	/**
	 * 姓名
	 */
	@NotBlank(message = "姓名不能为空")
	@ExcelProperty("姓名")
	private String name;
	
	
	/**
	 * 科室名称
	 */
	@NotBlank(message = "科室名称不能为空")
	@ExcelProperty("科室名称")
	private String department;
	
	/**
	 * 医疗机构名称
	 */
	@NotBlank(message = "医院名称不能为空")
	@ExcelProperty("所属医院")
	private String affiliatedHospital;
	
	
	/**
	 * 职位名称
	 */
	@NotBlank(message = "职位名称不能为空")
	@ExcelProperty("职位名称")
	private String position;
	
	/**
	 * 创建时间
	 */
	@ExcelProperty(value = "创建时间")
	private LocalDateTime createTime;
}
