package com.pig4cloud.pig.doctor.request;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.pig4cloud.plugin.excel.annotation.ExcelLine;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Data;

/**
 * <h3>doctor</h3>
 *
 * @author HuangJiayu
 * @description <p>批量导入医生Excel实体</p>
 * @date 2024-07-15 19:00
 **/
@Data
@ColumnWidth(30)
public class ImportDoctorBaseListRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ExcelLine
	@ExcelIgnore
	private Long lineNum;
	
	/**
	 * 主键ID
	 */
	@ExcelProperty("医生编号")
	private Long doctorUid;
	
	@ExcelProperty("医生姓名")
	private String doctorName;
	
	@NotBlank(message = "电话不能为空")
	@ExcelProperty("电话")
	private String doctorPhonenumber;
	
	@ExcelProperty("医生职务")
	private String position;
	
	@ExcelProperty("所属医疗机构")
	private String affiliatedHospital;
	
	@ExcelProperty("所属科室")
	private String department;
	
	@NotBlank(message = "系统用户名不能为空")
	@ExcelProperty("用户名")
	private String username;
	
}
