package com.pig4cloud.pig.patient.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.PatientNowDiseaseEntity;

public interface PatientNowDiseaseService extends IService<PatientNowDiseaseEntity> {

    JSONArray getPatientDiseaseAndTime(Long patientUid);
}