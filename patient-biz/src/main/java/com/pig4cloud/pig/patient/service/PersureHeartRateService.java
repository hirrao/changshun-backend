package com.pig4cloud.pig.patient.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;

import java.time.LocalDate;

public interface PersureHeartRateService extends IService<PersureHeartRateEntity> {
    JSONObject classifyAllBloodPressure(Long patientUid);
    String getCurrentRiskAssessment(Long patientUid);
    String judgeRiskByBloodPressure(float systolic, float diastolic);
    JSONObject classifyBloodPressure(Long patientUid);
    PersureHeartRateEntity getTodayMaxBloodPressure(Long patientUid);

    PersureHeartRateEntity getTodayMinHeartRate(Long patientUid);
    JSONObject getWeeklyPressureData(LocalDate date, int weeksAgo);
    JSONObject getMonthlyPressureData(LocalDate date, int monthsAgo);
}