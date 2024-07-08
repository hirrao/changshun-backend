package com.pig4cloud.pig.patient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import com.pig4cloud.pig.patient.entity.PressureAnomalyLogsEntity;

public interface PressureAnomalyLogsService extends IService<PressureAnomalyLogsEntity> {
    boolean createDailyRecord();
    int judgeBloodPressureAnomaly(long sdhId);
    boolean updateAnomalyCount(long sdhId);

    PersureHeartRateEntity getTodayMaxBloodPressure(Long patientUid);

    PersureHeartRateEntity getTodayMinHeartRate(Long patientUid);
}