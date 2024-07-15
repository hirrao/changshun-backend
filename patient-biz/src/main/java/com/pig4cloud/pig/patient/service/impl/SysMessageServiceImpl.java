package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.SysMessageEntity;
import com.pig4cloud.pig.patient.mapper.SysMessageMapper;
import com.pig4cloud.pig.patient.service.SysMessageService;
import com.pig4cloud.pig.patient.service.WebsocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统消息表
 *
 * @author 袁钰涛
 * @date 2024-07-05 23:52:47
 */
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessageEntity> implements SysMessageService {
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
        SysMessageEntity messageEntity = new SysMessageEntity();
        messageEntity.setDoctorUid(doctorUid);
        messageEntity.setPatientUid(patientUid);
        messageEntity.setMessageType("医生提醒");
        messageEntity.setJsonText(message);
        messageEntity.setSentDate(LocalDateTime.now());

        // 保存消息到数据库
        sysMessageMapper.insert(messageEntity);

        // 检查 WebSocket 会话是否存在
        if (!websocketService.sessionExists(String.valueOf(patientUid))) {
            log.error("WebSocket 会话不存在，无法发送消息给患者 ID：{}");
            return false;
        }

        // 通过 WebSocket 发送消息
        return websocketService.sendMsg(patientUid, message);
    }
}