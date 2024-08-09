package com.pig4cloud.pig.patient.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.PatientBmiManaEntity;

import java.util.Map;

public interface PatientBmiManaService extends IService<PatientBmiManaEntity> {
    JSONObject getPatientBmiStatus(Long patientUid);
    PatientBmiManaEntity getLastestRecoredByPatientUid(Long patientUid);
    Map<String, Float> getLatestWeightAndHeight(Long patientUid);
    JSONObject getNewestHeightWeightBmi(Long patientUid);
    JSONArray getLastestSevenBmiRecord(Long patientUid);

}