package com.pig4cloud.pig.patient.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.pig.patient.entity.SysMessageEntity;
import com.pig4cloud.pig.patient.mapper.PatientBaseMapper;
import com.pig4cloud.pig.patient.mapper.SysMessageMapper;
import com.pig4cloud.pig.patient.service.SysMessageService;
import com.pig4cloud.pig.patient.service.WebsocketService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统消息表
 *
 * @author 袁钰涛
 * @date 2024-07-05 23:52:47
 */
@Service
@Slf4j
public class SysMessageServiceImpl extends
 ServiceImpl<SysMessageMapper, SysMessageEntity> implements SysMessageService {
	
	@Autowired
	private PatientBaseMapper patientBaseMapper;
	
	@Override
	public boolean saveBatch(List<SysMessageEntity> entityList) {
		// 这里使用Mybatis Plus提供的saveBatch方法进行批量插入
		return saveBatch(entityList, 1000); // 这里的1000是批处理的大小，可以根据实际情况调整
	}
	
	@Autowired
	private SysMessageMapper sysMessageMapper;
	
	@Autowired
	private WebsocketService websocketService;
	
	@Override
	public boolean sendMessage(Long doctorUid, Long patientUid, String message) {
		// 创建消息实体
		try {
			SysMessageEntity messageEntity = new SysMessageEntity();
			messageEntity.setDoctorUid(doctorUid);
			messageEntity.setPatientUid(patientUid);
			messageEntity.setMessageType("医生提醒");
			messageEntity.setJsonText(message);
			messageEntity.setSentDate(LocalDateTime.now());
			
			// 保存消息到数据库
			sysMessageMapper.insert(messageEntity);
			
			// 通过websocket发送信息
			websocketService.sendMsg(patientUid, message);
			return true;
		} catch (Exception e) {
			log.error("医生发送消息错误：", e);
			return false;
		}
		
	}
	
	
	@Override
	public void sendMessage(Long doctorUid, Long patientUid, String messageType, String jsonText) {
		SysMessageEntity message = new SysMessageEntity();
		message.setDoctorUid(doctorUid);
		message.setPatientUid(patientUid);
		message.setMessageType(messageType);
		message.setJsonText(jsonText);
		message.setSentDate(LocalDateTime.now());
		
		sysMessageMapper.insert(message);
		
		// 发送消息到 WebSocket
		websocketService.sendMsg(patientUid, jsonText);
	}
	
	@Override
	public List<SysMessageEntity> getUnreadMessages(Long patientUid) {
		// 条件查询该用户未读的消息
		SysMessageEntity sysMessageEntity = new SysMessageEntity();
		sysMessageEntity.setPatientUid(patientUid);
		sysMessageEntity.setIsRead(false);
		LambdaQueryWrapper<SysMessageEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.setEntity(sysMessageEntity);
		return this.list(wrapper);
	}
	
	@Override
	public void markMessageAsRead(Long notificationId) {
		SysMessageEntity message = sysMessageMapper.selectById(notificationId);
		if (message != null) {
			message.setIsRead(true);
			sysMessageMapper.updateById(message);
		}
	}
	
	@Override
	public JSONArray getRecentMessageByDoctorId(Long doctorUid) {
		LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
		List<SysMessageEntity> messages = sysMessageMapper.findRecentMessageByDoctorId(doctorUid,
		 oneWeekAgo);
		JSONArray jsonArray = new JSONArray();
		
		for (SysMessageEntity message : messages) {
			QueryWrapper<PatientBaseEntity> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("patient_uid", message.getPatientUid());
			
			PatientBaseEntity patientBase = patientBaseMapper.selectOne(queryWrapper);
			
			JSONObject baseMsg = new JSONObject();
			
			// 处理日期格式
			LocalDateTime sentDate = message.getSentDate();
			LocalDate today = LocalDate.now();
			LocalDate yesterday = today.minusDays(1);
			
			if (sentDate.toLocalDate().equals(today)) {
				baseMsg.put("time",
				 "今天" + sentDate.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
			} else if (sentDate.toLocalDate().equals(yesterday)) {
				baseMsg.put("time",
				 "昨天" + sentDate.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
			} else {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M月d日 HH:mm");
				baseMsg.put("time", sentDate.format(formatter));
			}
			
			baseMsg.put("sex", patientBase.getSex());
			baseMsg.put("name", patientBase.getPatientName());
			baseMsg.put("message", message.getJsonText());
			baseMsg.put("age",
			 Period.between(patientBase.getBirthday(), LocalDate.now()).getYears());
			
			jsonArray.add(baseMsg);
		}
		return jsonArray;
	}
}