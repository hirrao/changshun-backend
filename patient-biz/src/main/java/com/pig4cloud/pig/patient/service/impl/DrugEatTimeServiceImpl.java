package com.pig4cloud.pig.patient.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.DrugEatTimeEntity;
import com.pig4cloud.pig.patient.entity.EatDrugAlertEntity;
import com.pig4cloud.pig.patient.mapper.DrugEatTimeMapper;
import com.pig4cloud.pig.patient.service.DrugEatTimeService;
import com.pig4cloud.pig.patient.service.EatDrugAlertService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用药时间对照表（因为一款药可能需要多个时间）
 *
 * @author 袁钰涛
 * @date 2024-07-05 09:57:07
 */
@Service
public class DrugEatTimeServiceImpl extends
 ServiceImpl<DrugEatTimeMapper, DrugEatTimeEntity> implements DrugEatTimeService {
	
	@Autowired
	private EatDrugAlertService eatDrugAlertService;
	
	@Override
	public JSONArray getTodayDrugEatTime(Long patientUid) {
		//  由于需要新加一个 isEaten 所以采用 JSONObject作为返回更好
		JSONArray res = new JSONArray();
		//	先查询用药计划，根据用药计划是否激活继续查询
		LambdaQueryWrapper<EatDrugAlertEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(EatDrugAlertEntity::getPatientUid, patientUid)
		 .eq(EatDrugAlertEntity::getIsActive, 1);
		List<EatDrugAlertEntity> eatDrugAlertEntities = eatDrugAlertService.list(wrapper);
		
		//	对于已经激活的用药计划，查询对应的用药时间，根据最后用药时间是否是今天确认是否需要服用
		for (EatDrugAlertEntity eatDrugAlertEntity : eatDrugAlertEntities) {
			//	查询用药时间
			LambdaQueryWrapper<DrugEatTimeEntity> drugEatTimeWrapper = Wrappers.lambdaQuery();
			drugEatTimeWrapper.eq(DrugEatTimeEntity::getPdeId, eatDrugAlertEntity.getPdeId());
			List<DrugEatTimeEntity> drugEatTimeEntities = this.list(drugEatTimeWrapper);
			//	判断是否需要服用
			for (DrugEatTimeEntity drugEatTimeEntity : drugEatTimeEntities) {
				JSONObject tmp = new JSONObject();
				tmp.put("patientUid", patientUid);
				tmp.put("drugName", eatDrugAlertEntity.getDrugName());
				tmp.put("unit", eatDrugAlertEntity.getUnit());
				tmp.put("dose", eatDrugAlertEntity.getDose());
				tmp.put("pdeId", eatDrugAlertEntity.getPdeId());
				tmp.put("pepId", drugEatTimeEntity.getPepId());
				tmp.put("eatTime", drugEatTimeEntity.getEatTime());
				//	判断是否是今天确认是否已经服用
				tmp.put("isEaten", isToday(drugEatTimeEntity.getLastEatTime()));
				res.add(tmp);
			}
		}
		//	最终将结果按照时间顺序排序（升序），开始排序
		res.sort((o1, o2) -> {
			JSONObject jo1 = (JSONObject) o1;
			JSONObject jo2 = (JSONObject) o2;
			return jo1.getString("eatTime").compareTo(jo2.getString("eatTime"));
		});
		return res;
		
	}
	
	@Override
	public void eatDrug(DrugEatTimeEntity drugEatTimeEntity) {
		//  先查询补充其余字段
		DrugEatTimeEntity tmp = this.getById(drugEatTimeEntity.getPepId());
		//	判断是否是今天
		if (isToday(tmp.getLastEatTime())) {
			//	如果是今天，直接返回
			return;
		}
		//	更新最后服用时间
		tmp.setLastEatTime(LocalDate.now());
		this.updateById(tmp);
	}
	
	//	判断输入日期是否是今天
	public static boolean isToday(LocalDate date) {
		return date.equals(LocalDate.now());
	}
}