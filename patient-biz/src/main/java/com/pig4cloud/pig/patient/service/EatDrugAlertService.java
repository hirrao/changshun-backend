package com.pig4cloud.pig.patient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.EatDrugAlertEntity;

import java.time.LocalDate;
import java.util.List;

public interface EatDrugAlertService extends IService<EatDrugAlertEntity> {
    List<EatDrugAlertEntity> getActiveAlerts();
    void sendDrugAlerts();

}