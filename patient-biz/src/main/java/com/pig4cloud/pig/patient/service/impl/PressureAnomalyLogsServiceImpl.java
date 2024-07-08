package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import com.pig4cloud.pig.patient.entity.PressureAnomalyLogsEntity;
import com.pig4cloud.pig.patient.mapper.PersureHeartRateMapper;
import com.pig4cloud.pig.patient.mapper.PressureAnomalyLogsMapper;
import com.pig4cloud.pig.patient.service.PersureHeartRateService;
import com.pig4cloud.pig.patient.service.PressureAnomalyLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 血压异常信息统计
 *
 * @author wangwenche
 * @date 2024-07-07 17:44:20
 */
@Service
public class PressureAnomalyLogsServiceImpl extends ServiceImpl<PressureAnomalyLogsMapper, PressureAnomalyLogsEntity> implements PressureAnomalyLogsService {

    @Autowired
    private PressureAnomalyLogsMapper pressureAnomalyLogsMapper;

    @Autowired
    private PersureHeartRateService persureHeartRateService;

    @Autowired
    private PersureHeartRateMapper persureHeartRateMapper;
    @Override
    public boolean createDailyRecord(){
        LocalDate today = LocalDate.now();
        PressureAnomalyLogsEntity newRecord = new PressureAnomalyLogsEntity();
        newRecord.setDate(today);
        newRecord.setSevere(0);
        newRecord.setModerate(0);
        newRecord.setMild(0);
        newRecord.setElevated(0);
        newRecord.setAllData(0);

        return pressureAnomalyLogsMapper.insert(newRecord) > 0;
    }

    @Override
    public int judgeBloodPressureAnomaly(long sdhId){
        PersureHeartRateEntity persureHeartRate = persureHeartRateMapper.selectById(sdhId);
        float systolic = persureHeartRate.getSystolic(); // 收缩压 高压
        float diastolic = persureHeartRate.getDiastolic(); // 舒张压 低压
        if(systolic >= 180 || diastolic >= 110){
            return 1;
        } else if(systolic >= 160 || diastolic >= 100){
            return 2;
        } else if(systolic >= 140 || diastolic >= 90){
            return 3;
        } else if(systolic >= 120 || diastolic >= 80){
            return 4;
        } else if(systolic >= 90 && diastolic >= 60){
            return 5;
        } else {
            return 6;
        }
    }

    @Override
    public boolean updateAnomalyCount(long sdhId){
        LocalDateTime dateTime = (persureHeartRateMapper.selectById(sdhId)).getUploadTime();
        Long patientUid = (persureHeartRateMapper.selectById(sdhId)).getPatientUid();
        LocalDate date = dateTime.toLocalDate();
        QueryWrapper<PressureAnomalyLogsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date", date)
                .eq("patient_uid", patientUid);

        PressureAnomalyLogsEntity record = pressureAnomalyLogsMapper.selectOne(queryWrapper);
        int type = judgeBloodPressureAnomaly(sdhId);

        switch (type){
            case 1: record.setSevere(record.getSevere() + 1); break;
            case 2: record.setModerate(record.getModerate() + 1); break;
            case 3: record.setMild(record.getMild() + 1); break;
            case 4: record.setElevated(record.getElevated() + 1); break;
            case 6: record.setLow(record.getLow() + 1); break;
        }

        record.setAllData(record.getAllData() + 1);

        return pressureAnomalyLogsMapper.updateById(record) > 0;
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