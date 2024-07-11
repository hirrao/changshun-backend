package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.alibaba.fastjson.JSONObject;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import com.pig4cloud.pig.patient.entity.PressureAnomalyEntity;
import com.pig4cloud.pig.patient.mapper.PatientDoctorMapper;
import com.pig4cloud.pig.patient.mapper.PersureHeartRateMapper;
import com.pig4cloud.pig.patient.mapper.PressureAnomalyMapper;
import com.pig4cloud.pig.patient.service.PersureHeartRateService;
import com.pig4cloud.pig.patient.service.PressureAnomalyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
/**
 * 血压异常次数统计
 *
 * @author wangwenche
 * @date 2024-07-08 11:20:07
 */
@Service
public class PressureAnomalyServiceImpl extends ServiceImpl<PressureAnomalyMapper, PressureAnomalyEntity> implements PressureAnomalyService {
    @Autowired
    private PressureAnomalyMapper pressureAnomalyMapper;

    @Autowired
    private PersureHeartRateService persureHeartRateService;

    @Autowired
    private PersureHeartRateMapper persureHeartRateMapper;

    @Autowired
    private PatientDoctorMapper patientDoctorMapper;

    @Override
    public PressureAnomalyEntity createDailyRecord(long patient_uid){
        LocalDate today = LocalDate.now();
        PressureAnomalyEntity newRecord = new PressureAnomalyEntity();
        newRecord.setDate(today);
        newRecord.setSevere(0);
        newRecord.setModerate(0);
        newRecord.setMild(0);
        newRecord.setElevated(0);
        newRecord.setAllNum(0);
        newRecord.setLow(0);
        newRecord.setPatientUid(patient_uid);
        pressureAnomalyMapper.insert(newRecord);
        return newRecord;
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


        QueryWrapper<PressureAnomalyEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date", date)
                .eq("patient_uid", patientUid);

        PressureAnomalyEntity record = pressureAnomalyMapper.selectOne(queryWrapper);
        if (record == null) {
            record = createDailyRecord(patientUid);
        }

        int type = judgeBloodPressureAnomaly(sdhId);

        switch (type){
            case 1: record.setSevere(record.getSevere() + 1); break;
            case 2: record.setModerate(record.getModerate() + 1); break;
            case 3: record.setMild(record.getMild() + 1); break;
            case 4: record.setElevated(record.getElevated() + 1); break;
            case 6: record.setLow(record.getLow() + 1); break;
        }

        record.setAllNum(record.getAllNum() + 1);

        return pressureAnomalyMapper.updateById(record) > 0;
    }

    @Override
    public JSONObject getWeekAnomalyCount(LocalDate date, int weekAgo){
        LocalDate startOfWeek = date.minusWeeks(weekAgo).with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        QueryWrapper<PressureAnomalyEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("date", startOfWeek, endOfWeek);

        List<PressureAnomalyEntity> weeklyRecords = pressureAnomalyMapper.selectList(queryWrapper);

        // 初始化计数
        JSONObject anomalyCounts = new JSONObject();
        anomalyCounts.put("Severe", 0);
        anomalyCounts.put("Moderate", 0);
        anomalyCounts.put("Mild", 0);
        anomalyCounts.put("Elevated", 0);
        anomalyCounts.put("Low", 0);
        anomalyCounts.put("AllNum", 0);

        // 检查是否有记录
        if(weeklyRecords.isEmpty()){
            anomalyCounts.put("message", "当周没有血压数据！");
            return anomalyCounts;
        }

        // 统计每个级别的次数
        for(PressureAnomalyEntity record : weeklyRecords){
            anomalyCounts.put("Severe", anomalyCounts.getInteger("Severe") + record.getSevere());
            anomalyCounts.put("Moderate", anomalyCounts.getInteger("Moderate") + record.getModerate());
            anomalyCounts.put("Mild", anomalyCounts.getInteger("Mild") + record.getMild());
            anomalyCounts.put("Elevated", anomalyCounts.getInteger("Elevated") + record.getElevated());
            anomalyCounts.put("Low", anomalyCounts.getInteger("Low") + record.getLow());
            anomalyCounts.put("AllNum", anomalyCounts.getInteger("AllNum") + record.getAllNum());
        }

        return anomalyCounts;
    }

    @Override
    public JSONObject getMonthAnomalyCount(LocalDate date, int monthAgo){
        YearMonth yearMonth = YearMonth.from(date).minusMonths(monthAgo);
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        QueryWrapper<PressureAnomalyEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("date", startOfMonth, endOfMonth);

        List<PressureAnomalyEntity> monthlyRecords = pressureAnomalyMapper.selectList(queryWrapper);

        // 初始化计数
        JSONObject anomalyCounts = new JSONObject();
        anomalyCounts.put("Severe", 0);
        anomalyCounts.put("Moderate", 0);
        anomalyCounts.put("Mild", 0);
        anomalyCounts.put("Elevated", 0);
        anomalyCounts.put("Low", 0);
        anomalyCounts.put("AllNum", 0);

        // 检查是否有记录
        if (monthlyRecords.isEmpty()) {
            anomalyCounts.put("message", "该月没有任何血压数据！");
            return anomalyCounts;
        }

        // 统计每个级别的次数
        for (PressureAnomalyEntity record : monthlyRecords) {
            anomalyCounts.put("Severe", anomalyCounts.getInteger("Severe") + record.getSevere());
            anomalyCounts.put("Moderate", anomalyCounts.getInteger("Moderate") + record.getModerate());
            anomalyCounts.put("Mild", anomalyCounts.getInteger("Mild") + record.getMild());
            anomalyCounts.put("Elevated", anomalyCounts.getInteger("Elevated") + record.getElevated());
            anomalyCounts.put("Low", anomalyCounts.getInteger("Low") + record.getLow());
            anomalyCounts.put("AllNum", anomalyCounts.getInteger("AllNum") + record.getAllNum());
        }

        return anomalyCounts;
    }

    @Override
    public PersureHeartRateEntity getTodayMaxBloodPressure(Long patientUid) {
        return persureHeartRateMapper.selectTodayMaxBloodPressure(patientUid);
    }

    @Override
    public PersureHeartRateEntity getTodayMinHeartRate(Long patientUid) {
        return persureHeartRateMapper.selectTodayMinHeartRate(patientUid);
    }

    @Override
    public JSONObject getAnomalyCountByDoctorUid(Long doctorUid) {
        QueryWrapper<PatientDoctorEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("doctor_uid", doctorUid);
        List<PatientDoctorEntity> patientDoctorEntities = patientDoctorMapper.selectList(queryWrapper);

        int severe = 0, moderate = 0, mild = 0, elevated = 0, low = 0, all = 0;

        for(PatientDoctorEntity patientDoctorEntity : patientDoctorEntities){
            QueryWrapper<PressureAnomalyEntity> queryWrapper1 = new QueryWrapper<>();
            LocalDate now = LocalDate.now();
            queryWrapper1.eq("patient_uid", patientDoctorEntity.getPatientUid())
                    .eq("date", now);
            List<PressureAnomalyEntity> anomalyRecords = pressureAnomalyMapper.selectList(queryWrapper1);

            for(PressureAnomalyEntity anomalyRecord : anomalyRecords){
                severe += anomalyRecord.getSevere();
                moderate += anomalyRecord.getModerate();
                mild += anomalyRecord.getMild();
                elevated += anomalyRecord.getElevated();
                low += anomalyRecord.getLow();
                all += anomalyRecord.getAllNum();
            }
        }

        JSONObject result = new JSONObject();
        result.put("severe", severe);
        result.put("moderate", moderate);
        result.put("mild", mild);
        result.put("elevated", elevated);
        result.put("low", low);
        result.put("all", all);

        return result;
    }
}