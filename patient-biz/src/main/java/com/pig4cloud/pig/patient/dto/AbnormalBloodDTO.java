package com.pig4cloud.pig.patient.dto;

import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import java.time.LocalDate;
import lombok.Data;

/**
 * <h3>patient</h3>
 *
 * @author HuangJiayu
 * @description <p>异常血压记录实体</p>
 * @date 2024-07-17 20:54
 **/
@Data
public class AbnormalBloodDTO extends PersureHeartRateEntity {
	
	//	单独添加查询日期，方便查看一天内所有数据
	private LocalDate queryDate;
}
