package com.pig4cloud.pig.patient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.alibaba.fastjson.JSONObject;
import com.pig4cloud.pig.patient.entity.PressureAnomalyEntity;

import java.time.LocalDate;

public interface PressureAnomalyService extends IService<PressureAnomalyEntity> {
    PressureAnomalyEntity createDailyRecord(long patient_uid);
    int judgeBloodPressureAnomaly(long sdhId);
    boolean updateAnomalyCount(long sdhId);
    JSONObject getWeekAnomalyCount(LocalDate date, int weekAgo);
    JSONObject getMonthAnomalyCount(LocalDate date, int monthAgo);
}