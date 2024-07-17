package com.pig4cloud.pig.patient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.EatDrugAlertEntity;
import com.pig4cloud.pig.patient.entity.SysMessageEntity;

import java.time.LocalDate;
import java.util.List;

public interface EatDrugAlertService extends IService<EatDrugAlertEntity> {
    List<EatDrugAlertEntity> getActiveAlerts();
    void sendDrugAlerts();
    void addSysMessage(SysMessageEntity sysMessage);

}