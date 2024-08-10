package com.pig4cloud.pig.patient.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pig4cloud.pig.patient.config.LocalTimeListDeserializer;
import com.pig4cloud.pig.patient.config.LocalTimeListSerializer;
import com.pig4cloud.pig.patient.entity.EatDrugAlertEntity;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <h3>patient</h3>
 *
 * @author HuangJiayu
 * @description <p>添加用药计划</p>
 * @date 2024-08-05 21:29
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public class AddDrugAlertRequest extends EatDrugAlertEntity {
	
	
	@JsonSerialize(using = LocalTimeListSerializer.class)
	@JsonDeserialize(using = LocalTimeListDeserializer.class)
	private List<LocalTime> eatTime;
}
