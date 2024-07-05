package com.pig4cloud.pig.patient.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.DrugEatTimeEntity;

public interface DrugEatTimeService extends IService<DrugEatTimeEntity> {
	
	/**
	 * 描述: 输入患者id，查询该患者的当天所有用药，并给出是否已经服用，最终需要按照时间顺序排序（升序）
	 */
	
	JSONArray getTodayDrugEatTime(Long patientUid);
	
	void eatDrug(DrugEatTimeEntity drugEatTimeEntity);
}