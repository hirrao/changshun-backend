package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.DrugEatTimeEntity;
import com.pig4cloud.pig.patient.entity.EatDrugAlertEntity;
import com.pig4cloud.pig.patient.entity.SysMessageEntity;
import com.pig4cloud.pig.patient.mapper.DrugEatTimeMapper;
import com.pig4cloud.pig.patient.mapper.EatDrugAlertMapper;
import com.pig4cloud.pig.patient.mapper.SysMessageMapper;
import com.pig4cloud.pig.patient.service.EatDrugAlertService;
import com.pig4cloud.pig.patient.service.WebsocketService;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
			// 遍历列表
			for (DrugEatTimeEntity item : list) {
				// note: 检测当天是否已经用药过，日期检测需要使用Period，因为日期最小单位是天而不是秒
				Period durationToday = Period.between(nowDate, item.getLastEatTime());
				if (durationToday.getDays() <= 0) {
					// 无需提醒
					continue;
				}
				// 检测用药时间和当前时间是否相差一小时
				LocalTime nowTime = LocalTime.now();
				Duration nowBetween = Duration.between(nowTime, item.getEatTime().toLocalTime());
				if (nowBetween.toHours() <= 1) {
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
	
}