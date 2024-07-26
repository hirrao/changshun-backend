package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.EatDrugAlertEntity;
import com.pig4cloud.pig.patient.entity.SysMessageEntity;
import com.pig4cloud.pig.patient.mapper.EatDrugAlertMapper;
import com.pig4cloud.pig.patient.mapper.SysMessageMapper;
import com.pig4cloud.pig.patient.service.DrugEatTimeService;
import com.pig4cloud.pig.patient.service.EatDrugAlertService;
import com.pig4cloud.pig.patient.service.WebsocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 用药管理表
 *
 * @author huangqing
 * @date 2024-07-05 01:50:45
 */
@Service
@Slf4j
public class EatDrugAlertServiceImpl extends ServiceImpl<EatDrugAlertMapper, EatDrugAlertEntity> implements EatDrugAlertService {

    @Autowired
    private EatDrugAlertMapper eatDrugAlertMapper;

    @Autowired
    private WebsocketService websocketService;

    @Autowired
    private SysMessageMapper sysMessageMapper;

    @Override
    public List<EatDrugAlertEntity> getActiveAlerts() {
        return eatDrugAlertMapper.selectList(new QueryWrapper<EatDrugAlertEntity>().eq("is_active", 1));
    }

    @Scheduled(cron = "0 0 * * * ?") // 每小时检查一次
    @Override
    public void sendDrugAlerts() {
        List<EatDrugAlertEntity> alerts = getActiveAlerts();
        for (EatDrugAlertEntity alert : alerts) {
            String message = String.format("请记得服用药物: %s，剂量: %d%s",
                    alert.getDrugName(), alert.getDose(), alert.getUnit());
            websocketService.sendMsg(alert.getPatientUid(), message);

            // 添加系统消息
            SysMessageEntity sysMessage = new SysMessageEntity();
            sysMessage.setDoctorUid(null);
            sysMessage.setPatientUid(alert.getPatientUid());
            sysMessage.setMessageType("用药提醒");
            sysMessage.setJsonText("{\"drugName\":\"" + alert.getDrugName() + "\",\"dose\":\"" + alert.getDose() + "\",\"unit\":\"" + alert.getUnit() + "\"}");
            sysMessage.setSentDate(LocalDateTime.now());
            sysMessage.setIsRead(false);
            addSysMessage(sysMessage);
        }
    }
    @Override
    public void addSysMessage(SysMessageEntity sysMessage) {
        sysMessageMapper.insert(sysMessage);
    }

}