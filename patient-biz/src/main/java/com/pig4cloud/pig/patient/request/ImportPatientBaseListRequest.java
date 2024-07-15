package com.pig4cloud.pig.patient.request;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.pig4cloud.plugin.excel.annotation.ExcelLine;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

/**
 * <h3>patient</h3>
 *
 * @author HuangJiayu
 * @description <p>使用Excel批量导入患者基本信息</p>
 * @date 2024-07-15 17:35
 **/
@Data
@ColumnWidth(30)
public class ImportPatientBaseListRequest implements Serializable {
	
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
	@ExcelProperty("患者编号")
	private Long patientUid;
	
	/**
	 * 身份证号码
	 */
	@ExcelProperty("身份证号码")
	private String identificationNumber;
	
	/**
	 * 患者姓名
	 */
	@ExcelProperty("患者姓名")
	private String patientName;
	
	@ExcelProperty("患者性别")
	private String sex;
	
	
	@ExcelProperty("出生日期")
	private LocalDate birthday;
	
	
	@NotBlank(message = "电话不能为空")
	@ExcelProperty("电话")
	private String phoneNumber;
	
	@NotBlank(message = "系统用户名不能为空")
	@ExcelProperty("系统用户名")
	private String username;
	
}
