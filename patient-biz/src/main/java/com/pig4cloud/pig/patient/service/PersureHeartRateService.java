package com.pig4cloud.pig.patient.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;

public interface PersureHeartRateService extends IService<PersureHeartRateEntity> {
    JSONObject classifyAllBloodPressure(Long patientUid);
    String getCurrentRiskAssessment(Long patientUid);
    String judgeRiskByBloodPressure(float systolic, float diastolic);
    JSONObject classifyBloodPressure(Long patientUid);
    PersureHeartRateEntity getTodayMaxBloodPressure(Long patientUid);

    PersureHeartRateEntity getTodayMinHeartRate(Long patientUid);
}