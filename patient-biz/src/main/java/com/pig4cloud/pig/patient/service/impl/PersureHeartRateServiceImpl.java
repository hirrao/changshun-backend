package com.pig4cloud.pig.patient.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import com.pig4cloud.pig.patient.mapper.PersureHeartRateMapper;
import com.pig4cloud.pig.patient.service.PersureHeartRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 血压心率展示
 *
 * @author wangwenche
 * @date 2024-07-04 20:02:22
 */
@Service
public class PersureHeartRateServiceImpl extends ServiceImpl<PersureHeartRateMapper, PersureHeartRateEntity> implements PersureHeartRateService {
    @Autowired
    private PersureHeartRateMapper persureHeartRateMapper;

    @Override
    public JSONObject classifyBloodPressure(Long patientUid){
        LambdaQueryWrapper<PersureHeartRateEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PersureHeartRateEntity::getPatientUid, patientUid);

        List<PersureHeartRateEntity> records = persureHeartRateMapper.selectList(queryWrapper);

        int severe = 0, moderate = 0, mild = 0, elevated = 0, normal = 0, all = 0;

        for(PersureHeartRateEntity record : records){
            float systolic = record.getSystolic(); // 收缩压 高压
            float diastolic = record.getDiastolic(); // 舒张压 低压
            if(systolic >= 180 || diastolic >= 110){
                severe++;
            } else if(systolic >= 160 || diastolic >= 100){
                moderate++;
            } else if(systolic >= 140 || diastolic >= 90){
                mild++;
            } else if(systolic >= 120 || diastolic >= 80){
                elevated++;
            } else{
                normal++;
            }
            all++;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("severe", severe);
        jsonObject.put("moderate", moderate);
        jsonObject.put("mild", mild);
        jsonObject.put("elevated", elevated);
        jsonObject.put("normal", normal);
        jsonObject.put("all", all);

        return jsonObject;
    }

    @Override
    public PersureHeartRateEntity getTodayMaxBloodPressure(Long patientUid) {
        return persureHeartRateMapper.selectTodayMaxBloodPressure(patientUid);
    }

    @Override
    public PersureHeartRateEntity getTodayMinHeartRate(Long patientUid) {
        return persureHeartRateMapper.selectTodayMinHeartRate(patientUid);
    }
}