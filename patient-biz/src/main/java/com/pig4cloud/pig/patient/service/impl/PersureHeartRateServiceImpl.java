package com.pig4cloud.pig.patient.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import com.pig4cloud.pig.patient.mapper.PatientBaseMapper;
import com.pig4cloud.pig.patient.mapper.PersureHeartRateMapper;
import com.pig4cloud.pig.patient.service.PersureHeartRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
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
    @Autowired
    private PatientBaseMapper patientBaseMapper;

    @Override
    public String judgeRiskByBloodPressure(float systolic, float diastolic){
        if(systolic >= 180 || diastolic >= 110){
            return "重度";
        } else if(systolic >= 160 || diastolic >= 100){
            return "中度";
        } else if(systolic >= 140 || diastolic >= 90){
            return "轻度";
        } else if(systolic >= 120 || diastolic >= 80){
            return "正常高值";
        } else if(systolic >= 90 && diastolic >= 60){
            return "正常";
        } else {
            return "偏低";
        }
    }

    @Override
    public JSONObject classifyAllBloodPressure(Long patientUid){
        LambdaQueryWrapper<PersureHeartRateEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PersureHeartRateEntity::getPatientUid, patientUid);

        List<PersureHeartRateEntity> records = persureHeartRateMapper.selectList(queryWrapper);

        int severe = 0, moderate = 0, mild = 0, elevated = 0, normal = 0, low = 0, all = 0;

        for(PersureHeartRateEntity record : records){
            float systolic = record.getSystolic(); // 收缩压 高压
            float diastolic = record.getDiastolic(); // 舒张压 低压

            String risk = judgeRiskByBloodPressure(systolic, diastolic);
            switch(risk){
                case "重度": severe++;
                case "中度": moderate++;
                case "轻度": mild++;
                case "正常高值": elevated++;
                case "偏低": low++;
            }
            all++;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("severe", severe);
        jsonObject.put("moderate", moderate);
        jsonObject.put("mild", mild);
        jsonObject.put("elevated", elevated);
        jsonObject.put("low", low);
        jsonObject.put("all", all);

        return jsonObject;
    }

    @Override
    public String getCurrentRiskAssessment(Long patientUid){
        LambdaQueryWrapper<PersureHeartRateEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PersureHeartRateEntity::getPatientUid, patientUid)
                .orderByDesc(PersureHeartRateEntity::getUploadTime)
                .last("limit 1");
        float systolic = (persureHeartRateMapper.selectOne(queryWrapper)).getSystolic(); // 高压
        float diastolic = (persureHeartRateMapper.selectOne(queryWrapper)).getDiastolic(); // 低压
        return judgeRiskByBloodPressure(systolic, diastolic);
    }

    @Override
    public JSONObject getWeeklyPressureData(LocalDate date, int weeksAgo, long patientUid) {
        LocalDate startOfWeek = date.minusWeeks(weeksAgo + 1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        QueryWrapper<PersureHeartRateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("upload_time", startOfWeek.atStartOfDay(), endOfWeek.atTime(23, 59, 59))
                .eq("patient_uid", patientUid);

        List<PersureHeartRateEntity> weeklyRecords = persureHeartRateMapper.selectList(queryWrapper);

        Map<LocalDate, List<PersureHeartRateEntity>> recordsByDate = weeklyRecords.stream()
                .collect(Collectors.groupingBy(record -> record.getUploadTime().toLocalDate()));

        JSONObject pressureData = new JSONObject();

        for (Map.Entry<LocalDate, List<PersureHeartRateEntity>> entry : recordsByDate.entrySet()) {
            LocalDate recordDate = entry.getKey();
            JSONArray systolicArray = new JSONArray();
            JSONArray diastolicArray = new JSONArray();

            for (PersureHeartRateEntity record : entry.getValue()) {
                systolicArray.add(record.getSystolic());
                diastolicArray.add(record.getDiastolic());
            }

            JSONObject dailyData = new JSONObject();
            dailyData.put("systolic", systolicArray);
            dailyData.put("diastolic", diastolicArray);

            pressureData.put(recordDate.toString(), dailyData);
        }

        return pressureData;
    }

    @Override
    public JSONObject getMonthlyPressureData(LocalDate date, int monthsAgo, long patientUid) {
        LocalDate startOfMonth = date.minusMonths(monthsAgo).withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());

        QueryWrapper<PersureHeartRateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("upload_time", startOfMonth.atStartOfDay(), endOfMonth.atTime(23, 59, 59))
                .eq("patient_uid", patientUid);

        List<PersureHeartRateEntity> monthlyRecords = persureHeartRateMapper.selectList(queryWrapper);

        Map<LocalDate, List<PersureHeartRateEntity>> recordsByDate = monthlyRecords.stream()
                .collect(Collectors.groupingBy(record -> record.getUploadTime().toLocalDate()));

        JSONObject pressureData = new JSONObject();


        for (Map.Entry<LocalDate, List<PersureHeartRateEntity>> entry : recordsByDate.entrySet()) {
            LocalDate recordDate = entry.getKey();
            JSONArray systolicArray = new JSONArray();
            JSONArray diastolicArray = new JSONArray();

            for (PersureHeartRateEntity record : entry.getValue()) {
                systolicArray.add(record.getSystolic());
                diastolicArray.add(record.getDiastolic());
            }

            JSONObject dailyData = new JSONObject();
            dailyData.put("systolic", systolicArray);
            dailyData.put("diastolic", diastolicArray);

            pressureData.put(recordDate.toString(), dailyData);
        }

        return pressureData;
    }

    @Override
    public JSONObject getYearlyPressureData(LocalDate date, int yearsAgo, long patientUid) {
        LocalDate startOfYear = date.minusYears(yearsAgo).withDayOfYear(1);
        LocalDate endOfYear = startOfYear.with(TemporalAdjusters.lastDayOfYear());

        QueryWrapper<PersureHeartRateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("upload_time", startOfYear.atStartOfDay(), endOfYear.atTime(23, 59, 59))
                .eq("patient_uid", patientUid);

        List<PersureHeartRateEntity> yearLyRecords = persureHeartRateMapper.selectList(queryWrapper);

        Map<LocalDate, List<PersureHeartRateEntity>> recordsByDate = yearLyRecords.stream()
                .collect(Collectors.groupingBy(record -> record.getUploadTime().toLocalDate()));

        JSONObject pressureData = new JSONObject();


        for (Map.Entry<LocalDate, List<PersureHeartRateEntity>> entry : recordsByDate.entrySet()) {
            LocalDate recordDate = entry.getKey();
            JSONArray systolicArray = new JSONArray();
            JSONArray diastolicArray = new JSONArray();

            for (PersureHeartRateEntity record : entry.getValue()) {
                systolicArray.add(record.getSystolic());
                diastolicArray.add(record.getDiastolic());
            }

            JSONObject dailyData = new JSONObject();
            dailyData.put("systolic", systolicArray);
            dailyData.put("diastolic", diastolicArray);

            pressureData.put(recordDate.toString(), dailyData);
        }

        return pressureData;
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
    public JSONArray getDailyConsecutiveAbnormalities(){
        LocalDate date = LocalDate.now();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        QueryWrapper<PersureHeartRateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("upload_time", startOfDay, endOfDay);

        List<PersureHeartRateEntity> todayRecords = persureHeartRateMapper.selectList(queryWrapper);

        // 建立patient_uid和PatientBaseEntity之间的映射，便于后续查询患者基本信息
        Map<Long, PatientBaseEntity> patientBaseMap = patientBaseMapper.selectList(new QueryWrapper<>())
                .stream()
                .collect(Collectors.toMap(PatientBaseEntity::getPatientUid, Function.identity()));

        // 建立patient_uid和PersureHeartRateEntity之间的映射，便于后续按患者统计心率血压异常次数
        Map<Long, List<PersureHeartRateEntity>> recordsByPatient = todayRecords.stream()
                .collect(Collectors.groupingBy(PersureHeartRateEntity::getPatientUid));

        JSONArray result = new JSONArray();

        for(Map.Entry<Long, List<PersureHeartRateEntity>> entry : recordsByPatient.entrySet()){
            Long patientUid = entry.getKey();
            List<PersureHeartRateEntity> records = entry.getValue();
            records.sort(Comparator.comparing(PersureHeartRateEntity::getUploadTime));

            int consecutiveHighBp = 0; // 连续高压超过180的次数
            int maxConsecutiveHighBp = 0; // 连续高压超过180的最大次数
            int consecutiveLowHr = 0; // 连续低压超过110的次数
            int maxConsecutiveLowHr = 0; // 连续低压超过110的最大次数

            for(PersureHeartRateEntity record : records){
                if(record.getSystolic() >= 180 || record.getDiastolic() >= 110){
                    consecutiveHighBp++;
                    maxConsecutiveHighBp = Math.max(maxConsecutiveLowHr, consecutiveLowHr);
                } else {
                    consecutiveHighBp = 0;
                }

                if(record.getHeartRate() < 55){
                    consecutiveLowHr++;
                    maxConsecutiveLowHr = Math.max(maxConsecutiveLowHr, consecutiveLowHr);
                } else {
                    consecutiveLowHr = 0;
                }
            }

            if(maxConsecutiveHighBp > 1 || maxConsecutiveLowHr > 1){
                JSONObject patientData = new JSONObject();
                PatientBaseEntity patientBase = patientBaseMap.get(patientUid);
                LocalDate birthday = patientBase.getBirthday();
                LocalDate current = LocalDate.now();
                int age = 0;
                if (birthday != null && current != null) {
                    age =  Period.between(birthday, current).getYears();
                }

                patientData.put("name", patientBase.getPatientName());
                patientData.put("sex", patientBase.getSex());
                patientData.put("age", age);

                // 一个患者可能既血压高又心率低，这时这两种信息仍然存在一个JSON中
                if(maxConsecutiveHighBp > 1){
                    patientData.put("abnormality", "血压过高");
                    patientData.put("count", maxConsecutiveHighBp);
                }

                if(maxConsecutiveLowHr > 1){
                    patientData.put("abnormality", "心率过低");
                    patientData.put("count", maxConsecutiveLowHr);
                }

                result.add(patientData);
            }
        }

        result.sort((a, b) -> ((Integer) ((JSONObject) b).get("count")).compareTo((Integer) ((JSONObject) a).get("count")));
        return result;
    }



    @Override
    public int countPatientsWithLowHeartRate(Long doctorUid) {
        return persureHeartRateMapper.countPatientsWithLowHeartRate(doctorUid);
    }

    @Override
    public int countPatientsWithNormalHeartRate(Long doctorUid) {
        return persureHeartRateMapper.countPatientsWithNormalHeartRate(doctorUid);
    }

    @Override
    public int countPatientsWithHighHeartRate(Long doctorUid) {
        return persureHeartRateMapper.countPatientsWithHighHeartRate(doctorUid);
    }

    @Override
    public int ccountPatientsWithLowHeartRate(Long doctorUid) {
        return persureHeartRateMapper.ccountPatientsWithLowHeartRate(doctorUid);
    }

    @Override
    public int ccountPatientsWithNormalHeartRate(Long doctorUid) {
        return persureHeartRateMapper.ccountPatientsWithNormalHeartRate(doctorUid);
    }

    @Override
    public int ccountPatientsWithHighHeartRate(Long doctorUid) {
        return persureHeartRateMapper.ccountPatientsWithHighHeartRate(doctorUid);
    }

    @Override
    public void updateSdhClassification(Long sdhId, Long patientUid) {
        persureHeartRateMapper.updateSdhClassification(sdhId, patientUid);
    }

    @Override
    public List<Map<String, Object>> countSdhClassificationByDoctorAndCare(Long doctorUid) {
        return persureHeartRateMapper.countSdhClassificationByDoctorAndCare(doctorUid);
    }

    @Override
    public List<Map<String, Object>> nocountSdhClassificationByDoctorAndCare(Long doctorUid) {
        return persureHeartRateMapper.nocountSdhClassificationByDoctorAndCare(doctorUid);
    }

    @Override
    public List<Map<String, Object>> getRecentTenDaysStatistics(Long doctorUid) {
        return baseMapper.getRecentTenDaysStatistics(doctorUid);
    }
}