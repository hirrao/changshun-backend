package com.pig4cloud.pig.patient.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.PatientBmiManaEntity;
import com.pig4cloud.pig.patient.mapper.PatientBmiManaMapper;
import com.pig4cloud.pig.patient.service.PatientBmiManaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 患者身高体重
 *
 * @author huangqing
 * @date 2024-07-05 01:15:51
 */
@Service
public class PatientBmiManaServiceImpl extends ServiceImpl<PatientBmiManaMapper, PatientBmiManaEntity> implements PatientBmiManaService {
    @Autowired
    private PatientBmiManaMapper patientBmiManaMapper;

    @Override
    public PatientBmiManaEntity getLastestRecoredByPatientUid(Long patientUid){
        LambdaQueryWrapper<PatientBmiManaEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PatientBmiManaEntity::getPatientUid, patientUid)
                .orderByDesc(PatientBmiManaEntity::getBmimeasurementDate)
                .last("limit 1");
        return patientBmiManaMapper.selectOne(queryWrapper);

    }

    @Override
    public Map<String, Float> getLatestWeightAndHeight(Long patientUid){
        PatientBmiManaEntity latestRecord = getLastestRecoredByPatientUid(patientUid);

        if(latestRecord == null){
            return null;
        }

        Map<String, Float> result = new HashMap<>();
        result.put("weight", latestRecord.getWeight());
        result.put("height", latestRecord.getHeight());

        return result;
    }

    @Override
    public JSONObject getPatientBmiStatus(Long patientUid){
        Map<String, Float> result = getLatestWeightAndHeight(patientUid);
        if(result == null){
            return null;
        }

        float bmi = result.get("weight") / (result.get("height") * result.get("height"));
        int bmiStatus = 0;
        if(bmi < 18.5){
            bmiStatus = 1;
        } else if (bmi < 23) {
            bmiStatus = 2;
        } else if (bmi < 25) {
            bmiStatus = 3;
        } else if (bmi < 30) {
            bmiStatus = 4;
        } else {
            bmiStatus = 5;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bmiStatus", bmiStatus);
        return jsonObject;
    }

    @Override
    public JSONObject getNewestHeightWeightBmi(Long patientUid) {
        Map<String, Object> latestData = patientBmiManaMapper.getLatestHeightWeight(patientUid);
        float height = (float) latestData.get("height"); // 单位为米
        float weight = (float) latestData.get("weight"); // 单位为千克
        String measurementDate = latestData.get("bmimeasurement_date").toString();

        float bmi = 0;
        if (height > 0) {
            bmi = weight / (height * height);
        }

        JSONObject result = new JSONObject();
        result.put("height", height);
        result.put("weight", weight);
        result.put("bmi", bmi);
        result.put("measurementDate", measurementDate);
        return result;
    }
}