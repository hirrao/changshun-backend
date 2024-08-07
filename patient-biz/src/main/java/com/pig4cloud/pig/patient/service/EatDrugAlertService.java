package com.pig4cloud.pig.patient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.entity.EatDrugAlertEntity;
import com.pig4cloud.pig.patient.entity.SysMessageEntity;

import com.pig4cloud.pig.patient.request.AddDrugAlertRequest;
import java.time.LocalDate;
import java.util.List;

public interface EatDrugAlertService extends IService<EatDrugAlertEntity> {
    List<EatDrugAlertEntity> getActiveAlerts();
    void sendDrugAlerts();
    void addSysMessage(SysMessageEntity sysMessage);
    R addDrugAlert(AddDrugAlertRequest eatDrugAlert);
    R getDrugList(Long patientUid);
    R deleteDrugAlertBatch(List<Long> alertIdList);
}