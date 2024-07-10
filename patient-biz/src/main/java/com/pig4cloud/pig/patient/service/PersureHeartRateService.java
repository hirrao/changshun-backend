package com.pig4cloud.pig.patient.service;

import com.alibaba.fastjson.JSONArray;
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

    JSONArray getDailyConsecutiveAbnormalities();

    int countPatientsWithLowHeartRate(Long doctorUid);

    int countPatientsWithNormalHeartRate(Long doctorUid);

    int countPatientsWithHighHeartRate(Long doctorUid);

    int ccountPatientsWithLowHeartRate(Long doctorUid);

    int ccountPatientsWithNormalHeartRate(Long doctorUid);

    int ccountPatientsWithHighHeartRate(Long doctorUid);

    void updateSdhClassification(Long sdhId, Long patientUid);
}