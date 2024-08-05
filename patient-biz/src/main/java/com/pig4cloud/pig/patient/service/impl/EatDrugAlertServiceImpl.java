package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.entity.DrugEatTimeEntity;
import com.pig4cloud.pig.patient.entity.EatDrugAlertEntity;
import com.pig4cloud.pig.patient.entity.SysMessageEntity;
import com.pig4cloud.pig.patient.mapper.DrugEatTimeMapper;
import com.pig4cloud.pig.patient.mapper.EatDrugAlertMapper;
import com.pig4cloud.pig.patient.mapper.SysMessageMapper;
import com.pig4cloud.pig.patient.request.AddDrugAlertRequest;
import com.pig4cloud.pig.patient.service.EatDrugAlertService;
import com.pig4cloud.pig.patient.service.WebsocketService;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用药管理表
 *
 * @author huangqing
 * @date 2024-07-05 01:50:45
 */
@Service
@Slf4j
public class EatDrugAlertServiceImpl extends
 ServiceImpl<EatDrugAlertMapper, EatDrugAlertEntity> implements EatDrugAlertService {
	
	@Autowired
	private EatDrugAlertMapper eatDrugAlertMapper;
	
	@Autowired
	private DrugEatTimeMapper drugEatTimeMapper;
	
	@Autowired
	private WebsocketService websocketService;
	
	@Autowired
	private SysMessageMapper sysMessageMapper;
	
	@Override
	public List<EatDrugAlertEntity> getActiveAlerts() {
		return eatDrugAlertMapper.selectList(
		 new QueryWrapper<EatDrugAlertEntity>().eq("is_active", 1));
	}
	
	@Scheduled(cron = "0 0/30 * * * ?") // 每半个小时检查一次
	@Override
	public void sendDrugAlerts() {
		List<EatDrugAlertEntity> alerts = getActiveAlerts();
		for (EatDrugAlertEntity alert : alerts) {
			// 根据计划寻找用药提醒时间
			LambdaQueryWrapper<DrugEatTimeEntity> wrapper = new LambdaQueryWrapper<>();
			DrugEatTimeEntity tmp = new DrugEatTimeEntity();
			tmp.setPdeId(alert.getPdeId());
			wrapper.setEntity(tmp);
			List<DrugEatTimeEntity> list = drugEatTimeMapper.selectList(wrapper);
			LocalDate nowDate = LocalDate.now();
			// 获取吃药频率
			String frequency = alert.getFrequency();
			int frequencyDays = getFrequencyDays(frequency);
			// 遍历列表
			for (DrugEatTimeEntity item : list) {
				// note: 检测当天是否已经用药过，日期检测需要使用Period，因为日期最小单位是天而不是秒
				Period durationToday = Period.between(nowDate, item.getLastEatTime());
				// 需要根据时间频率和上次用药时间一起推算是否需要提醒
				// note: 计算的时间是旧时间-新时间，所以是负数，应该需要取绝对值保证时间都是正数
				if (Math.abs(durationToday.getDays()) <= frequencyDays) {
					// 无需提醒
					continue;
				}
				// 检测用药时间和当前时间是否相差一小时
				LocalTime nowTime = LocalTime.now();
				Duration nowBetween = Duration.between(nowTime, item.getEatTime().toLocalTime());
				if (Math.abs(nowBetween.toHours()) <= 1) {
					// 提醒
					String message = String.format("请记得服用药物: %s，剂量: %d%s，时间:%s",
					 alert.getDrugName(), alert.getDose(), alert.getUnit(), item.getEatTime());
					websocketService.sendMsg(alert.getPatientUid(), message);
					LocalDateTime now = LocalDateTime.now();
					// 添加系统消息
					SysMessageEntity sysMessage = getSysMessageEntity(alert, now,
					 item.getEatTime());
					addSysMessage(sysMessage);
				}
				
			}
			
			
		}
	}
	
	private static int getFrequencyDays(String frequency) {
		//	根据提供的字符串计算应该相隔几天
		if (frequency.equals("每天")) {
			return 0;
		} else {
			//  获取数字部分
			String regEx = "[^0-9]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(frequency);
			String trim = m.replaceAll("").trim();
			return Integer.parseInt(trim);
		}
	}
	
	private static @NotNull SysMessageEntity getSysMessageEntity(EatDrugAlertEntity alert,
	 LocalDateTime now, Time alertTime) {
		SysMessageEntity sysMessage = new SysMessageEntity();
		sysMessage.setDoctorUid(null);
		sysMessage.setPatientUid(alert.getPatientUid());
		sysMessage.setMessageType("用药提醒");
		sysMessage.setJsonText(
		 "{\"drugName\":\"" + alert.getDrugName() + "\",\"dose\":\"" + alert.getDose()
		  + "\",\"unit\":\"" + alert.getUnit() + "\"," + "\"eatTime:\":" + "\""
		  + alertTime.toString() + "\"" + "}");
		sysMessage.setSentDate(now);
		sysMessage.setIsRead(false);
		return sysMessage;
	}
	
	@Override
	public void addSysMessage(SysMessageEntity sysMessage) {
		sysMessageMapper.insert(sysMessage);
	}
	
	@Transactional
	@Override
	public R addDrugAlert(AddDrugAlertRequest eatDrugAlert) {
		//	先添加基本用药计划
		this.save(eatDrugAlert);
		//	添加之后获得pde_id，添加对应时间
		List<DrugEatTimeEntity> tmpList = new ArrayList<>();
		for (LocalTime localTime : eatDrugAlert.getEatTime()) {
			//	添加提醒时间
			DrugEatTimeEntity tmp = new DrugEatTimeEntity();
			tmp.setPdeId(eatDrugAlert.getPdeId());
			tmp.setPatientUid(eatDrugAlert.getPatientUid());
			tmp.setEatTime(Time.valueOf(localTime));
			tmpList.add(tmp);
		}
		// 检查是否为空数组,不为空直接整体添加
		if (!tmpList.isEmpty()) {
			//	批量插入到drug_eat_time表中
			for (DrugEatTimeEntity drugEatTimeEntity : tmpList) {
				drugEatTimeMapper.insert(drugEatTimeEntity);
			}
		}
		return R.ok();
		
	}
	
	@Override
	public R getDrugList(Long patientUid) {
		// 根据患者id查询所有用药计划，以及对应用药时间（排序）
		// 查询所有用药计划
		LambdaQueryWrapper<EatDrugAlertEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(EatDrugAlertEntity::getPatientUid, patientUid);
		List<EatDrugAlertEntity> eatDrugAlertEntityList = this.list(wrapper);
		// 设置返回值数组
		List<AddDrugAlertRequest> res = new ArrayList<>();
		// 根据每个用药计划找寻对应的时间
		for (EatDrugAlertEntity eatDrugAlertEntity : eatDrugAlertEntityList) {
			DrugEatTimeEntity tmp = new DrugEatTimeEntity();
			tmp.setPatientUid(eatDrugAlertEntity.getPatientUid());
			tmp.setPdeId(eatDrugAlertEntity.getPdeId());
			LambdaQueryWrapper<DrugEatTimeEntity> drugEatTimeEntityLambdaQueryWrapper =
			 new LambdaQueryWrapper<>();
			drugEatTimeEntityLambdaQueryWrapper.setEntity(tmp);
			List<DrugEatTimeEntity> drugEatTimeEntities = drugEatTimeMapper.selectList(
			 drugEatTimeEntityLambdaQueryWrapper);
			//	添加用药时间
			List<LocalTime> resEatTime = new ArrayList<>();
			for (DrugEatTimeEntity drugEatTimeEntity : drugEatTimeEntities) {
				resEatTime.add(drugEatTimeEntity.getEatTime().toLocalTime());
			}
			// 按照时间顺序进行排序
			resEatTime.sort(LocalTime::compareTo);
			
			AddDrugAlertRequest tmpRes = new AddDrugAlertRequest();
			tmpRes.setPdeId(eatDrugAlertEntity.getPdeId());
			tmpRes.setPatientUid(eatDrugAlertEntity.getPatientUid());
			tmpRes.setDrugName(eatDrugAlertEntity.getDrugName());
			tmpRes.setFrequency(eatDrugAlertEntity.getFrequency());
			tmpRes.setUnit(eatDrugAlertEntity.getUnit());
			tmpRes.setDose(eatDrugAlertEntity.getDose());
			tmpRes.setIsActive(eatDrugAlertEntity.getIsActive());
			tmpRes.setEatTime(resEatTime);
			res.add(tmpRes);
		}
		return R.ok(res);
	}
	
}