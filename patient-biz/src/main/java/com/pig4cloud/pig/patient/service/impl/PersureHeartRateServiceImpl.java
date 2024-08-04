package com.pig4cloud.pig.patient.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import com.pig4cloud.pig.patient.entity.PressureAnomalyEntity;
import com.pig4cloud.pig.patient.mapper.PatientBaseMapper;
import com.pig4cloud.pig.patient.mapper.PatientDoctorMapper;
import com.pig4cloud.pig.patient.mapper.PersureHeartRateMapper;
import com.pig4cloud.pig.patient.service.PersureHeartRateService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
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
    @Autowired
    private PatientDoctorMapper patientDoctorMapper;

    @Override
    public boolean savePersureHeartRate(PersureHeartRateEntity persureHeartRate) {
        String riskAssessment = judgeRiskByBloodPressure(persureHeartRate.getSystolic(), persureHeartRate.getDiastolic());
        persureHeartRate.setRiskAssessment(riskAssessment);
        return save(persureHeartRate);
    }

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

        int severe = 0, moderate = 0, mild = 0, elevated = 0, low = 0, all = 0;

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
    public JSONArray getWeeklyPressureHeartRateData(int weeksAgo, Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDate startOfWeek = date.minusWeeks(weeksAgo + 1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        QueryWrapper<PersureHeartRateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("upload_time", startOfWeek.atStartOfDay(), endOfWeek.atTime(23, 59, 59))
                .eq("patient_uid", patientUid);

        List<PersureHeartRateEntity> weeklyRecords = persureHeartRateMapper.selectList(queryWrapper);

        Map<LocalDate, List<PersureHeartRateEntity>> recordsByDate = weeklyRecords.stream()
                .collect(Collectors.groupingBy(record -> record.getUploadTime().toLocalDate()));

        Map<LocalDate, JSONObject> sortedDailyData = new TreeMap<>();

        for (Map.Entry<LocalDate, List<PersureHeartRateEntity>> entry : recordsByDate.entrySet()) {
            LocalDate recordDate = entry.getKey();
            JSONArray systolicArray = new JSONArray();
            JSONArray diastolicArray = new JSONArray();
            JSONArray heartRateArray = new JSONArray();

            for (PersureHeartRateEntity record : entry.getValue()) {
                systolicArray.add(record.getSystolic() != null ? record.getSystolic() : null);
                diastolicArray.add(record.getDiastolic() != null ? record.getDiastolic() : null);
                // heartRateArray.add(record.getHeartRate() != null ? record.getHeartRate() : null);
            }

            JSONObject dailyData = new JSONObject();
            dailyData.put("systolic", systolicArray);
            dailyData.put("diastolic", diastolicArray);
            dailyData.put("heartRate", heartRateArray);
            dailyData.put("date", recordDate);

            sortedDailyData.put(recordDate, dailyData);
        }

        // 确保一周的每天都有数据，即使其中某天没有血压和心率数据
        for (LocalDate day = startOfWeek; !day.isAfter(endOfWeek); day = day.plusDays(1)) {
            if (!sortedDailyData.containsKey(day)) {
                JSONObject emptyData = new JSONObject();
                emptyData.put("systolic", new JSONArray());
                emptyData.put("diastolic", new JSONArray());
                emptyData.put("heartRate", new JSONArray());
                emptyData.put("date", day);
                sortedDailyData.put(day, emptyData);
            }
        }
        JSONArray pressureData = new JSONArray();
        pressureData.addAll(sortedDailyData.values());

        return pressureData;
    }

    @Override
    public JSONArray getMonthlyPressureHeartRateData(int monthsAgo, Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDate startOfMonth = date.minusMonths(monthsAgo).withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());

        QueryWrapper<PersureHeartRateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("upload_time", startOfMonth.atStartOfDay(), endOfMonth.atTime(23, 59, 59))
                .eq("patient_uid", patientUid);

        List<PersureHeartRateEntity> monthlyRecords = persureHeartRateMapper.selectList(queryWrapper);

        Map<LocalDate, List<PersureHeartRateEntity>> recordsByDate = monthlyRecords.stream()
                .collect(Collectors.groupingBy(record -> record.getUploadTime().toLocalDate()));

        Map<LocalDate, JSONObject> sortedDailyData = new TreeMap<>();

        for (Map.Entry<LocalDate, List<PersureHeartRateEntity>> entry : recordsByDate.entrySet()) {
            LocalDate recordDate = entry.getKey();
            JSONArray systolicArray = new JSONArray();
            JSONArray diastolicArray = new JSONArray();
            JSONArray heartRateArray = new JSONArray();

            for (PersureHeartRateEntity record : entry.getValue()) {
                systolicArray.add(record.getSystolic() != null ? record.getSystolic() : null);
                diastolicArray.add(record.getDiastolic() != null ? record.getDiastolic() : null);
                // heartRateArray.add(record.getHeartRate() != null ? record.getHeartRate() : null);
            }

            JSONObject dailyData = new JSONObject();
            dailyData.put("systolic", systolicArray);
            dailyData.put("diastolic", diastolicArray);
            dailyData.put("heartRate", heartRateArray);
            dailyData.put("date", recordDate);

            sortedDailyData.put(recordDate, dailyData);
        }

        for (LocalDate day = startOfMonth; !day.isAfter(endOfMonth); day = day.plusDays(1)) {
            if (!sortedDailyData.containsKey(day)) {
                JSONObject emptyData = new JSONObject();
                emptyData.put("systolic", new JSONArray());
                emptyData.put("diastolic", new JSONArray());
                emptyData.put("heartRate", new JSONArray());
                emptyData.put("date", day);
                sortedDailyData.put(day, emptyData);
            }
        }
        JSONArray pressureData = new JSONArray();
        pressureData.addAll(sortedDailyData.values());

        return pressureData;
    }


    @Override
    public JSONArray getYearlyPressureHeartRateData(int yearsAgo, Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDate startOfYear = date.minusYears(yearsAgo).withDayOfYear(1);
        LocalDate endOfYear = startOfYear.with(TemporalAdjusters.lastDayOfYear());

        QueryWrapper<PersureHeartRateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("upload_time", startOfYear.atStartOfDay(), endOfYear.atTime(23, 59, 59))
                .eq("patient_uid", patientUid);

        List<PersureHeartRateEntity> yearlyRecords = persureHeartRateMapper.selectList(queryWrapper);

        Map<LocalDate, List<PersureHeartRateEntity>> recordsByDate = yearlyRecords.stream()
                .collect(Collectors.groupingBy(record -> record.getUploadTime().toLocalDate()));

        Map<LocalDate, JSONObject> sortedDailyData = new TreeMap<>();

        for (Map.Entry<LocalDate, List<PersureHeartRateEntity>> entry : recordsByDate.entrySet()) {
            LocalDate recordDate = entry.getKey();
            JSONArray systolicArray = new JSONArray();
            JSONArray diastolicArray = new JSONArray();
            JSONArray heartRateArray = new JSONArray();

            for (PersureHeartRateEntity record : entry.getValue()) {
                systolicArray.add(record.getSystolic() != null ? record.getSystolic() : null);
                diastolicArray.add(record.getDiastolic() != null ? record.getDiastolic() : null);
                // heartRateArray.add(record.getHeartRate() != null ? record.getHeartRate() : null);
            }

            JSONObject dailyData = new JSONObject();
            dailyData.put("systolic", systolicArray);
            dailyData.put("diastolic", diastolicArray);
            dailyData.put("heartRate", heartRateArray);
            dailyData.put("date", recordDate);

            sortedDailyData.put(recordDate, dailyData);
        }

        for (LocalDate day = startOfYear; !day.isAfter(endOfYear); day = day.plusDays(1)) {
            if (!sortedDailyData.containsKey(day)) {
                JSONObject emptyData = new JSONObject();
                emptyData.put("systolic", new JSONArray());
                emptyData.put("diastolic", new JSONArray());
                emptyData.put("heartRate", new JSONArray());
                emptyData.put("date", day);
                sortedDailyData.put(day, emptyData);
            }
        }
        JSONArray pressureData = new JSONArray();
        pressureData.addAll(sortedDailyData.values());

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

//    @Override
//    public JSONArray getDailyConsecutiveAbnormalities(Long doctorUid) {
//        LocalDate date = LocalDate.now();
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.atTime(23, 59, 59);
//
//        QueryWrapper<PatientDoctorEntity> doctorQueryWrapper = new QueryWrapper<>();
//        doctorQueryWrapper.eq("doctor_uid", doctorUid);
//
//        List<Long> patientUids = patientDoctorMapper.selectList(doctorQueryWrapper)
//                .stream()
//                .map(PatientDoctorEntity::getPatientUid)
//                .toList();
//
//        if (patientUids.isEmpty()) {
//            return new JSONArray();
//        }
//
//        // 根据患者UID列表查询心率记录
//        QueryWrapper<PersureHeartRateEntity> heartRateQueryWrapper = new QueryWrapper<>();
//        heartRateQueryWrapper.between("upload_time", startOfDay, endOfDay)
//                .in("patient_uid", patientUids);
//
//
//
//        List<PersureHeartRateEntity> todayRecords = persureHeartRateMapper.selectList(heartRateQueryWrapper);
//
//        // 建立patient_uid和PatientBaseEntity之间的映射，便于后续查询患者基本信息
//        Map<Long, PatientBaseEntity> patientBaseMap = patientBaseMapper.selectList(new QueryWrapper<>())
//                .stream()
//                .collect(Collectors.toMap(PatientBaseEntity::getPatientUid, Function.identity()));
//
//        // 建立patient_uid和PersureHeartRateEntity之间的映射，便于后续按患者统计心率血压异常次数
//        Map<Long, List<PersureHeartRateEntity>> recordsByPatient = todayRecords.stream()
//                .collect(Collectors.groupingBy(PersureHeartRateEntity::getPatientUid));
//
//        JSONArray result = new JSONArray();
//
//        for (Map.Entry<Long, List<PersureHeartRateEntity>> entry : recordsByPatient.entrySet()) {
//            Long patientUid = entry.getKey();
//            List<PersureHeartRateEntity> records = entry.getValue();
//            records.sort(Comparator.comparing(PersureHeartRateEntity::getUploadTime));
//
//            List<JSONObject> patientDataList = new ArrayList<>();
//
//            int consecutiveHighBp = 0; // 连续高压超过180的次数
//            LocalDateTime highBpStart = null;
//            LocalDateTime highBpEnd = null;
//
//            int consecutiveLowHr = 0; // 连续低压超过110的次数
//            LocalDateTime lowHrStart = null;
//            LocalDateTime lowHrEnd = null;
//
//            for (PersureHeartRateEntity record : records) {
//                if (record.getSystolic() >= 180 || record.getDiastolic() >= 110) {
//                    if (consecutiveHighBp == 0) {
//                        highBpStart = record.getUploadTime();
//                    }
//                    consecutiveHighBp++;
//                    highBpEnd = record.getUploadTime();
//                } else {
//                    if (consecutiveHighBp > 1) {
//                        JSONObject patientData = new JSONObject();
//                        PatientBaseEntity patientBase = patientBaseMap.get(patientUid);
//                        LocalDate birthday = patientBase.getBirthday();
//                        LocalDate current = LocalDate.now();
//                        int age = 0;
//                        if (birthday != null) {
//                            age = Period.between(birthday, current).getYears();
//                        }
//
//                        patientData.put("name", patientBase.getPatientName());
//                        patientData.put("sex", patientBase.getSex());
//                        patientData.put("age", age);
//                        patientData.put("abnormality", "血压高于180/110mmHg");
//                        patientData.put("ill", "高压过高");
//                        patientData.put("count", consecutiveHighBp);
//                        patientData.put("duration", String.format("%d小时%d分钟",
//                                Duration.between(highBpStart, highBpEnd).toHours(),
//                                Duration.between(highBpStart, highBpEnd).toMinutes() % 60));
//
//                        patientDataList.add(patientData);
//                    }
//                    consecutiveHighBp = 0;
//                }
//
//                if (record.getHeartRate() < 60) {
//                    if (consecutiveLowHr == 0) {
//                        lowHrStart = record.getUploadTime();
//                    }
//                    consecutiveLowHr++;
//                    lowHrEnd = record.getUploadTime();
//                } else {
//                    if (consecutiveLowHr > 1) {
//                        JSONObject patientData = new JSONObject();
//                        PatientBaseEntity patientBase = patientBaseMap.get(patientUid);
//                        LocalDate birthday = patientBase.getBirthday();
//                        LocalDate current = LocalDate.now();
//                        int age = 0;
//                        if (birthday != null) {
//                            age = Period.between(birthday, current).getYears();
//                        }
//
//                        patientData.put("name", patientBase.getPatientName());
//                        patientData.put("sex", patientBase.getSex());
//                        patientData.put("age", age);
//                        patientData.put("abnormality", "心率低于60次/分钟");
//                        patientData.put("ill", "心率过低");
//                        patientData.put("count", consecutiveLowHr);
//                        patientData.put("duration", String.format("%d小时%d分钟",
//                                Duration.between(lowHrStart, lowHrEnd).toHours(),
//                                Duration.between(lowHrStart, lowHrEnd).toMinutes() % 60));
//
//                        patientDataList.add(patientData);
//                    }
//                    consecutiveLowHr = 0;
//                }
//            }
//
//            // 处理最后一条记录后的未记录异常情况
//            if (consecutiveHighBp > 1) {
//                JSONObject patientData = new JSONObject();
//                PatientBaseEntity patientBase = patientBaseMap.get(patientUid);
//                LocalDate birthday = patientBase.getBirthday();
//                LocalDate current = LocalDate.now();
//                int age = 0;
//                if (birthday != null) {
//                    age = Period.between(birthday, current).getYears();
//                }
//
//                patientData.put("name", patientBase.getPatientName());
//                patientData.put("sex", patientBase.getSex());
//                patientData.put("age", age);
//                patientData.put("abnormality", "血压高于180/110mmHg");
//                patientData.put("ill", "高压过高");
//                patientData.put("count", consecutiveHighBp);
//                patientData.put("duration", String.format("%d小时%d分钟",
//                        Duration.between(highBpStart, highBpEnd).toHours(),
//                        Duration.between(highBpStart, highBpEnd).toMinutes() % 60));
//
//                patientDataList.add(patientData);
//            }
//
//            if (consecutiveLowHr > 1) {
//                JSONObject patientData = new JSONObject();
//                PatientBaseEntity patientBase = patientBaseMap.get(patientUid);
//                LocalDate birthday = patientBase.getBirthday();
//                LocalDate current = LocalDate.now();
//                int age = 0;
//                if (birthday != null) {
//                    age = Period.between(birthday, current).getYears();
//                }
//
//                patientData.put("name", patientBase.getPatientName());
//                patientData.put("sex", patientBase.getSex());
//                patientData.put("age", age);
//                patientData.put("abnormality", "心率低于60次/分钟");
//                patientData.put("ill", "心率过低");
//                patientData.put("count", consecutiveLowHr);
//                patientData.put("duration", String.format("%d小时%d分钟",
//                        Duration.between(lowHrStart, lowHrEnd).toHours(),
//                        Duration.between(lowHrStart, lowHrEnd).toMinutes() % 60));
//
//                patientDataList.add(patientData);
//            }
//
//            result.addAll(patientDataList);
//        }
//
//        result.sort((a, b) -> ((Integer) ((JSONObject) b).get("count")).compareTo((Integer) ((JSONObject) a).get("count")));
//        return result;
//    }


    public JSONObject getMaxMinAvgSystolic(LocalDateTime start, LocalDateTime end, Long patientUid) {
        QueryWrapper<PersureHeartRateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_uid", patientUid)
                .between("upload_time", start, end);

        List<PersureHeartRateEntity> records = persureHeartRateMapper.selectList(queryWrapper);

        DoubleSummaryStatistics systolicStats = records.stream()
                .mapToDouble(PersureHeartRateEntity::getSystolic)
                .summaryStatistics();

        Optional<PersureHeartRateEntity> maxSystolicRecord = records.stream()
                .max((record1, record2) -> Float.compare(record1.getSystolic(), record2.getSystolic()));
        Optional<PersureHeartRateEntity> minSystolicRecord = records.stream()
                .min((record1, record2) -> Float.compare(record1.getSystolic(), record2.getSystolic()));

        JSONObject result = new JSONObject();
        result.put("max_systolic", maxSystolicRecord.map(PersureHeartRateEntity::getSystolic).orElse(null));
        result.put("max_diastolic", maxSystolicRecord.map(PersureHeartRateEntity::getDiastolic).orElse(null));
        result.put("min_systolic", minSystolicRecord.map(PersureHeartRateEntity::getSystolic).orElse(null));
        result.put("min_diastolic", minSystolicRecord.map(PersureHeartRateEntity::getDiastolic).orElse(null));
        result.put("avg_systolic", systolicStats.getAverage());

        return result;
    }

    public JSONObject getMaxMinAvgDiastolic(LocalDateTime start, LocalDateTime end, Long patientUid) {
        QueryWrapper<PersureHeartRateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_uid", patientUid)
                .between("upload_time", start, end);

        List<PersureHeartRateEntity> records = persureHeartRateMapper.selectList(queryWrapper);

        DoubleSummaryStatistics diastolicStats = records.stream()
                .mapToDouble(PersureHeartRateEntity::getDiastolic)
                .summaryStatistics();

        Optional<PersureHeartRateEntity> maxDiastolicRecord = records.stream()
                .max((record1, record2) -> Float.compare(record1.getDiastolic(), record2.getDiastolic()));
        Optional<PersureHeartRateEntity> minDiastolicRecord = records.stream()
                .min((record1, record2) -> Float.compare(record1.getDiastolic(), record2.getDiastolic()));

        JSONObject result = new JSONObject();
        result.put("max_diastolic", maxDiastolicRecord.map(PersureHeartRateEntity::getDiastolic).orElse(null));
        result.put("max_systolic", maxDiastolicRecord.map(PersureHeartRateEntity::getSystolic).orElse(null));
        result.put("min_diastolic", minDiastolicRecord.map(PersureHeartRateEntity::getDiastolic).orElse(null));
        result.put("min_systolic", minDiastolicRecord.map(PersureHeartRateEntity::getSystolic).orElse(null));
        result.put("avg_diastolic", diastolicStats.getAverage());

        return result;
    }

    @Override
    public JSONObject getDailyMaxMinAvgSystolic(Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return getMaxMinAvgSystolic(startOfDay, endOfDay, patientUid);
    }

    @Override
    public JSONObject getDailyMaxMinAvgDiastolic(Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return getMaxMinAvgDiastolic(startOfDay, endOfDay, patientUid);
    }

    public JSONObject getWeeklyMaxMinAvgSystolic(Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDateTime startOfWeek = date.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = date.with(DayOfWeek.SUNDAY).atTime(23, 59, 59);
        return getMaxMinAvgSystolic(startOfWeek, endOfWeek, patientUid);
    }

    @Override
    public JSONObject getWeeklyMaxMinAvgDiastolic(Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDateTime startOfWeek = date.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = date.with(DayOfWeek.SUNDAY).atTime(23, 59, 59);
        return getMaxMinAvgDiastolic(startOfWeek, endOfWeek, patientUid);
    }

    @Override
    public JSONObject getMonthlyMaxMinAvgSystolic(Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDateTime startOfMonth = date.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);
        return getMaxMinAvgSystolic(startOfMonth, endOfMonth, patientUid);
    }

    @Override
    public JSONObject getMonthlyMaxMinAvgDiastolic(Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDateTime startOfMonth = date.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);
        return getMaxMinAvgDiastolic(startOfMonth, endOfMonth, patientUid);
    }

    @Override
    public JSONObject getYearlyMaxMinAvgSystolic(Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDateTime startOfYear = date.withDayOfYear(1).atStartOfDay();
        LocalDateTime endOfYear = date.with(TemporalAdjusters.lastDayOfYear()).atTime(23, 59, 59);
        return getMaxMinAvgSystolic(startOfYear, endOfYear, patientUid);
    }

    @Override
    public JSONObject getYearlyMaxMinAvgDiastolic(Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDateTime startOfYear = date.withDayOfYear(1).atStartOfDay();
        LocalDateTime endOfYear = date.with(TemporalAdjusters.lastDayOfYear()).atTime(23, 59, 59);
        return getMaxMinAvgDiastolic(startOfYear, endOfYear, patientUid);
    }

    public JSONObject getMaxMinAvgPressureDiff(LocalDateTime start, LocalDateTime end, Long patientUid) {
        QueryWrapper<PersureHeartRateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_uid", patientUid)
                .between("upload_time", start, end);

        List<PersureHeartRateEntity> records = persureHeartRateMapper.selectList(queryWrapper);

        DoubleSummaryStatistics pulsePressureStats = records.stream()
                .mapToDouble(record -> record.getSystolic() - record.getDiastolic())
                .summaryStatistics();

        Optional<PersureHeartRateEntity> maxPulsePressureRecord = records.stream()
                .max((record1, record2) -> Float.compare(record1.getSystolic() - record1.getDiastolic(), record2.getSystolic() - record2.getDiastolic()));
        Optional<PersureHeartRateEntity> minPulsePressureRecord = records.stream()
                .min((record1, record2) -> Float.compare(record1.getSystolic() - record1.getDiastolic(), record2.getSystolic() - record2.getDiastolic()));

        JSONObject result = new JSONObject();
        result.put("max_pulse_pressure", maxPulsePressureRecord.map(record -> record.getSystolic() - record.getDiastolic()).orElse(null));
        result.put("max_pulse_pressure_systolic", maxPulsePressureRecord.map(PersureHeartRateEntity::getSystolic).orElse(null));
        result.put("max_pulse_pressure_diastolic", maxPulsePressureRecord.map(PersureHeartRateEntity::getDiastolic).orElse(null));
        result.put("min_pulse_pressure", minPulsePressureRecord.map(record -> record.getSystolic() - record.getDiastolic()).orElse(null));
        result.put("min_pulse_pressure_systolic", minPulsePressureRecord.map(PersureHeartRateEntity::getSystolic).orElse(null));
        result.put("min_pulse_pressure_diastolic", minPulsePressureRecord.map(PersureHeartRateEntity::getDiastolic).orElse(null));
        result.put("avg_pulse_pressure", pulsePressureStats.getAverage());

        return result;
    }

    @Override
    public JSONObject getDailyMaxMinAvgPressureDiff(Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return getMaxMinAvgPressureDiff(startOfDay, endOfDay, patientUid);
    }

    @Override
    public JSONObject getWeeklyMaxMinAvgPressureDiff(Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDateTime startOfWeek = date.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime endOfWeek = date.with(DayOfWeek.SUNDAY).atTime(23, 59, 59);
        return getMaxMinAvgPressureDiff(startOfWeek, endOfWeek, patientUid);
    }

    @Override
    public JSONObject getMonthlyMaxMinAvgPressureDiff(Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDateTime startOfMonth = date.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);
        return getMaxMinAvgPressureDiff(startOfMonth, endOfMonth, patientUid);
    }

    @Override
    public JSONObject getYearlyMaxMinAvgPressureDiff(Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDateTime startOfYear = date.withDayOfYear(1).atStartOfDay();
        LocalDateTime endOfYear = date.with(TemporalAdjusters.lastDayOfYear()).atTime(23, 59, 59);
        return getMaxMinAvgPressureDiff(startOfYear, endOfYear, patientUid);
    }

//    @Override
//    public JSONObject getDailyAveragePressureHeartRate(LocalDate date, Long patientUid) {
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.atTime(23, 59, 59);
//
//        QueryWrapper<PersureHeartRateEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("patient_uid", patientUid)
//                .between("upload_time", startOfDay, endOfDay);
//
//        List<PersureHeartRateEntity> records = persureHeartRateMapper.selectList(queryWrapper);
//        JSONObject result = new JSONObject();
//
//        if (records.isEmpty()) {
//            result.put("avg_systolic", null);
//            result.put("avg_diastolic", null);
//            result.put("avg_heartRate", null);
//        } else {
//            DoubleSummaryStatistics systolicStats = records.stream()
//                    .mapToDouble(PersureHeartRateEntity::getSystolic)
//                    .summaryStatistics();
//
//            DoubleSummaryStatistics diastolicStats = records.stream()
//                    .mapToDouble(PersureHeartRateEntity::getDiastolic)
//                    .summaryStatistics();
//
//            DoubleSummaryStatistics heartRateStats = records.stream()
//                    .mapToDouble(PersureHeartRateEntity::getHeartRate)
//                    .summaryStatistics();
//
//
//            result.put("avg_systolic", systolicStats.getCount() > 0 ? systolicStats.getAverage() : null);
//            result.put("avg_diastolic", diastolicStats.getCount() > 0 ? diastolicStats.getAverage() : null);
//            result.put("avg_heartRate", heartRateStats.getCount() > 0 ? heartRateStats.getAverage() : null);
//        }
//
//        return result;
//    }

//    @Override
//    public JSONArray getWeeklyAveragePressureHeartRateByDay(int weeksAgo, Long patientUid) {
//        LocalDate date = LocalDate.now();
//        LocalDate startOfWeek = date.minusWeeks(weeksAgo + 1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
//        LocalDate endOfWeek = startOfWeek.plusDays(6);
//
//        JSONArray result = new JSONArray();
//
//        for (LocalDate currentDate = startOfWeek; !currentDate.isAfter(endOfWeek); currentDate = currentDate.plusDays(1)) {
//            JSONObject dailyAverage = getDailyAveragePressureHeartRate(currentDate, patientUid);
//            dailyAverage.put("date", currentDate);
//
//            result.add(dailyAverage);
//        }
//
//        return result;
//    }

//    @Override
//    // 一个月的开头和结尾几天可能不是完整的一周，也算作一周
//    public JSONArray getMonthlyAveragePressureHeartRateByWeek(int monthsAgo, Long patientUid) {
//        LocalDate date = LocalDate.now();
//        LocalDate startOfMonth = date.minusMonths(monthsAgo).withDayOfMonth(1);
//        LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());
//
//        JSONArray monthlyPressureData = new JSONArray();
//
//        LocalDate startOfWeek = startOfMonth;
//        while (!startOfWeek.isAfter(endOfMonth)) {
//            LocalDate endOfWeek = startOfWeek.with(DayOfWeek.SUNDAY);
//            if (endOfWeek.isAfter(endOfMonth)) {
//                endOfWeek = endOfMonth;
//            }
//
//            LocalDateTime startDateTime = startOfWeek.atStartOfDay();
//            LocalDateTime endDateTime = endOfWeek.atTime(23, 59, 59);
//
//            QueryWrapper<PersureHeartRateEntity> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("patient_uid", patientUid)
//                    .between("upload_time", startDateTime, endDateTime);
//
//            List<PersureHeartRateEntity> weeklyRecords = persureHeartRateMapper.selectList(queryWrapper);
//
//            DoubleSummaryStatistics systolicStats = weeklyRecords.stream()
//                    .mapToDouble(PersureHeartRateEntity::getSystolic)
//                    .summaryStatistics();
//
//            DoubleSummaryStatistics diastolicStats = weeklyRecords.stream()
//                    .mapToDouble(PersureHeartRateEntity::getDiastolic)
//                    .summaryStatistics();
//
//            DoubleSummaryStatistics heartRateStats = weeklyRecords.stream()
//                    .mapToDouble(PersureHeartRateEntity::getHeartRate)
//                    .summaryStatistics();
//
//            JSONObject weeklyAverage = new JSONObject();
//            weeklyAverage.put("start_date", startOfWeek.toString());
//            weeklyAverage.put("end_date", endOfWeek.toString());
//            weeklyAverage.put("avg_systolic", systolicStats.getCount() > 0 ? systolicStats.getAverage() : null);
//            weeklyAverage.put("avg_diastolic", diastolicStats.getCount() > 0 ? diastolicStats.getAverage() : null);
//            weeklyAverage.put("avg_heartRate", heartRateStats.getCount() > 0 ? heartRateStats.getAverage() : null);
//            monthlyPressureData.add(weeklyAverage);
//
//            // 下一周
//            startOfWeek = endOfWeek.plusDays(1);
//        }
//
//        return monthlyPressureData;
//    }

//    @Override
//    public JSONArray getYearlyAveragePressureHeartRateByMonth(int yearsAgo, Long patientUid) {
//        LocalDate date = LocalDate.now();
//        LocalDate startOfYear = date.minusYears(yearsAgo).withDayOfYear(1);
//        int year = startOfYear.getYear();
//
//        JSONArray yearlyPressureData = new JSONArray();
//        for (int month = 1; month <= 12; month++) {
//            LocalDate startOfMonth = LocalDate.of(year, month, 1);
//            LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());
//
//            LocalDateTime startDateTime = startOfMonth.atStartOfDay();
//            LocalDateTime endDateTime = endOfMonth.atTime(23, 59, 59);
//
//            QueryWrapper<PersureHeartRateEntity> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("patient_uid", patientUid)
//                    .between("upload_time", startDateTime, endDateTime);
//
//            List<PersureHeartRateEntity> monthlyRecords = persureHeartRateMapper.selectList(queryWrapper);
//
//            DoubleSummaryStatistics systolicStats = monthlyRecords.stream()
//                    .mapToDouble(PersureHeartRateEntity::getSystolic)
//                    .summaryStatistics();
//
//            DoubleSummaryStatistics diastolicStats = monthlyRecords.stream()
//                    .mapToDouble(PersureHeartRateEntity::getDiastolic)
//                    .summaryStatistics();
//
//            DoubleSummaryStatistics heartRateStats = monthlyRecords.stream()
//                    .mapToDouble(PersureHeartRateEntity::getHeartRate)
//                    .summaryStatistics();
//
//            JSONObject monthlyAverage = new JSONObject();
//            monthlyAverage.put("month", startOfMonth.getMonth().toString());
//            monthlyAverage.put("avg_systolic", systolicStats.getCount() > 0 ? systolicStats.getAverage() : null);
//            monthlyAverage.put("avg_diastolic", diastolicStats.getCount() > 0 ? diastolicStats.getAverage() : null);
//            monthlyAverage.put("avg_heartRate", heartRateStats.getCount() > 0 ? heartRateStats.getAverage() : null);
//
//
//            yearlyPressureData.add(monthlyAverage);
//        }
//        return yearlyPressureData;
//    }

    @Override
    public JSONObject getNewlyPressureHeartRateData(Long patientUid) {
        PersureHeartRateEntity measure =  persureHeartRateMapper.getLatestMeasurement(patientUid);
        JSONObject data = new JSONObject();
        data.put("收缩压", measure.getSystolic());
        data.put("舒张压", measure.getDiastolic());
        // data.put("心率", measure.getHeartRate());
        data.put("风险评估", measure.getRiskAssessment());
        data.put("时间", measure.getUploadTime());
        return data;
    }

//    @Override
//    public int countPatientsWithLowHeartRate(Long doctorUid) {
//        return persureHeartRateMapper.countPatientsWithLowHeartRate(doctorUid);
//    }

//    @Override
//    public int countPatientsWithNormalHeartRate(Long doctorUid) {
//        return persureHeartRateMapper.countPatientsWithNormalHeartRate(doctorUid);
//    }
//
//    @Override
//    public int countPatientsWithHighHeartRate(Long doctorUid) {
//        return persureHeartRateMapper.countPatientsWithHighHeartRate(doctorUid);
//    }
//
//    @Override
//    public int ccountPatientsWithLowHeartRate(Long doctorUid) {
//        return persureHeartRateMapper.ccountPatientsWithLowHeartRate(doctorUid);
//    }
//
//    @Override
//    public int ccountPatientsWithNormalHeartRate(Long doctorUid) {
//        return persureHeartRateMapper.ccountPatientsWithNormalHeartRate(doctorUid);
//    }
//
//    @Override
//    public int ccountPatientsWithHighHeartRate(Long doctorUid) {
//        return persureHeartRateMapper.ccountPatientsWithHighHeartRate(doctorUid);
//    }

    @Override
    public void updateSdhClassification(Long sdhId, Long patientUid) {
        persureHeartRateMapper.updateSdhClassification(sdhId, patientUid);
    }

    @Override
    public JSONObject countSdhClassificationByDoctorAndCare(Long doctorUid) {
        List<Map<String, Object>> result = persureHeartRateMapper.countSdhClassificationByDoctorAndCare(doctorUid);

        List<Integer> levelOneCounts = Arrays.asList(0, 0, 0);
        List<Integer> levelTwoCounts = Arrays.asList(0, 0, 0);
        List<Integer> levelThreeCounts = Arrays.asList(0, 0, 0);

        int total = 0, levelOneTotal = 0, levelTwoTotal = 0, levelThreeTotal = 0;

        for(Map<String, Object> row : result){
            String classification = (String) row.get("sdh_classification");
            Integer count = ((Long) row.get("count")).intValue();
            total += count;

            if(classification.contains("一级高血压")) {
                levelOneTotal += count;
                if(classification.contains("低危")){
                    levelOneCounts.set(0, count);
                } else if(classification.contains("中危")){
                    levelOneCounts.set(1, count);
                } else if(classification.contains("高危")){
                    levelOneCounts.set(2, count);
                }
            } else if (classification.contains("二级高血压")) {
                levelTwoTotal += count;
                if (classification.contains("低危")) {
                    levelTwoCounts.set(0, count);
                } else if (classification.contains("中危")) {
                    levelTwoCounts.set(1, count);
                } else if (classification.contains("高危")) {
                    levelTwoCounts.set(2, count);
                }
            } else if (classification.contains("三级高血压")) {
                levelThreeTotal += count;
                if (classification.contains("低危")) {
                    levelThreeCounts.set(0, count);
                } else if (classification.contains("中危")) {
                    levelThreeCounts.set(1, count);
                } else if (classification.contains("高危")) {
                    levelThreeCounts.set(2, count);
                }
            }
        }

        Map<String, Object> mapData = new LinkedHashMap<>();
        mapData.put("累计", total);
        mapData.put("一级高血压共", levelOneTotal);
        mapData.put("二级高血压共", levelTwoTotal);
        mapData.put("三级高血压共", levelThreeTotal);
        mapData.put("一级高血压", levelOneCounts);
        mapData.put("二级高血压", levelTwoCounts);
        mapData.put("三级高血压", levelThreeCounts);

        JSONObject data = new JSONObject(mapData);

        return data;
    }

    @Override
    public JSONObject nocountSdhClassificationByDoctorAndCare(Long doctorUid) {
        List<Map<String, Object>> result = persureHeartRateMapper.nocountSdhClassificationByDoctorAndCare(doctorUid);

        List<Integer> levelOneCounts = Arrays.asList(0, 0, 0);
        List<Integer> levelTwoCounts = Arrays.asList(0, 0, 0);
        List<Integer> levelThreeCounts = Arrays.asList(0, 0, 0);

        int total = 0, levelOneTotal = 0, levelTwoTotal = 0, levelThreeTotal = 0;
        for(Map<String, Object> row : result){
            String classification = (String) row.get("sdh_classification");
            Integer count = ((Long) row.get("count")).intValue();
            total += count;

            if(classification.contains("一级高血压")) {
                levelOneTotal += count;
                if(classification.contains("低危")){
                    levelOneCounts.set(0, count);
                } else if(classification.contains("中危")){
                    levelOneCounts.set(1, count);
                } else if(classification.contains("高危")){
                    levelOneCounts.set(2, count);
                }
            } else if (classification.contains("二级高血压")) {
                levelTwoTotal += count;
                if (classification.contains("低危")) {
                    levelTwoCounts.set(0, count);
                } else if (classification.contains("中危")) {
                    levelTwoCounts.set(1, count);
                } else if (classification.contains("高危")) {
                    levelTwoCounts.set(2, count);
                }
            } else if (classification.contains("三级高血压")) {
                levelThreeTotal += count;
                if (classification.contains("低危")) {
                    levelThreeCounts.set(0, count);
                } else if (classification.contains("中危")) {
                    levelThreeCounts.set(1, count);
                } else if (classification.contains("高危")) {
                    levelThreeCounts.set(2, count);
                }
            }
        }

        Map<String, Object> mapData = new LinkedHashMap<>();
        mapData.put("累计", total);
        mapData.put("一级高血压共", levelOneTotal);
        mapData.put("二级高血压共", levelTwoTotal);
        mapData.put("三级高血压共", levelThreeTotal);
        mapData.put("一级高血压", levelOneCounts);
        mapData.put("二级高血压", levelTwoCounts);
        mapData.put("三级高血压", levelThreeCounts);

        JSONObject data = new JSONObject(mapData);

        return data;
    }

    @Override
    public Map<String, Long> getDailyStatistics(Long doctorUid) {
        // Fetch the results from the database
        List<Map<String, Object>> resultList = persureHeartRateMapper.selectDailyStatistics(doctorUid);
        Map<String, Long> statisticsMap = new LinkedHashMap<>();

        // Initialize map with 0 counts for the last 10 days
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (int i = 9; i >= 0; i--) {
            String dateKey = today.minusDays(i).format(formatter);
            statisticsMap.put(dateKey, 0L);
        }

        // Fill in actual counts from database results
        for (Map<String, Object> result : resultList) {
            String date = result.get("date").toString();

            // Assuming the date is in yyyy-MM-dd format
            LocalDate localDate = LocalDate.parse(date);

            // Calculate the formatted date
            String formattedDate = localDate.format(formatter);

            // Check if the formattedDate is in the last 10 days
            if (statisticsMap.containsKey(formattedDate)) {
                Long count = ((Number) result.get("count")).longValue();
                statisticsMap.put(formattedDate, count);
            }
        }

        return statisticsMap;
    }


//    @Override
//    public JSONObject getHeartRateStatistics(Long doctorUid) {
//        int normal = 0, low = 0, high = 0, all = 0;
//        normal = countPatientsWithNormalHeartRate(doctorUid);
//        low = countPatientsWithLowHeartRate(doctorUid);
//        high = countPatientsWithHighHeartRate(doctorUid);
//        all = normal + low + high;
//
//        Map<String, Object> mapData = new LinkedHashMap<>();
//        mapData.put("正常", normal);
//        mapData.put("过缓", low);
//        mapData.put("过急", high);
//        mapData.put("累计", all);
//
//        JSONObject result = new JSONObject(mapData);
//        return result;
//    }

//    @Override
//    public JSONObject getcareHeartRateStatistics(Long doctorUid) {
//        int normal = 0, low = 0, high = 0, all = 0;
//        normal = ccountPatientsWithNormalHeartRate(doctorUid);
//        low = ccountPatientsWithLowHeartRate(doctorUid);
//        high = ccountPatientsWithHighHeartRate(doctorUid);
//        all = normal + low + high;
//
//        Map<String, Object> mapData = new LinkedHashMap<>();
//        mapData.put("正常", normal);
//        mapData.put("过缓", low);
//        mapData.put("过急", high);
//        mapData.put("累计", all);
//
//        JSONObject result = new JSONObject(mapData);
//        return result;
//    }

    @Override
    public JSONObject getRiskAssessmentNum(Long patientUid, LocalDate date) {
        List<Map<String, Object>> riskAssessmentCounts = persureHeartRateMapper.getRiskAssessmentCountByDate(patientUid, date);

        String[] allRiskLevels = {"重度", "中度", "轻度", "正常高值", "正常", "偏低"};
        JSONObject result = new JSONObject();

        for (String level : allRiskLevels) {
            result.put(level, 0);
        }

        for (Map<String, Object> record : riskAssessmentCounts) {
            String riskAssessment = (String) record.get("risk_assessment");
            Integer count = ((Long) record.get("count")).intValue();
            result.put(riskAssessment, count);
        }

        return result;
    }

    @Override
    public JSONObject getLastSevenDayAnomalyNum(Long patientUid) {
        List<Map<String, Object>> riskAssessmentCounts = persureHeartRateMapper.getRiskAssessmentCountLastSevenDays(patientUid);

        String[] allRiskLevels = {"重度", "中度", "轻度", "正常高值", "正常", "偏低"};
        JSONObject result = new JSONObject();

        for (String level : allRiskLevels) {
            result.put(level, 0);
        }

        for (Map<String, Object> record : riskAssessmentCounts) {
            String riskAssessment = (String) record.get("risk_assessment");
            Integer count = ((Long) record.get("count")).intValue(); // 将 count 转换为 Integer
            result.put(riskAssessment, count);
        }

        return result;
    }

    @Override
    public JSONObject getWeekAnomalyCount(Long patientUid, int weeksAgo) {
        List<Map<String, Object>> riskAssessmentCounts = persureHeartRateMapper.getRiskAssessmentCountByWeek(patientUid, weeksAgo);

        // 预定义所有可能的风险评估等级
        String[] allRiskLevels = {"重度", "中度", "轻度", "正常高值", "正常", "偏低"};
        JSONObject result = new JSONObject();

        // 初始化所有风险等级为0
        for (String level : allRiskLevels) {
            result.put(level, 0);
        }

        // 填充查询结果
        for (Map<String, Object> record : riskAssessmentCounts) {
            String riskAssessment = (String) record.get("risk_assessment");
            Integer count = ((Long) record.get("count")).intValue(); // 将 count 转换为 Integer
            result.put(riskAssessment, count);
        }

        return result;
    }

//    @Override
//    public JSONObject getAnomalyCountByDoctorUid(Long doctorUid, boolean care) {
//        QueryWrapper<PatientDoctorEntity> queryWrapper = new QueryWrapper<>();
//        if(care){
//            queryWrapper.eq("doctor_uid", doctorUid)
//                    .eq("care", true);
//        } else{
//            queryWrapper.eq("doctor_uid", doctorUid);
//        }
//        List<PatientDoctorEntity> patientDoctorEntities = patientDoctorMapper.selectList(queryWrapper);
//
//        int severe = 0, moderate = 0, mild = 0, elevated = 0, low = 0, all = 0;
//
//        for(PatientDoctorEntity patientDoctorEntity : patientDoctorEntities){
//            QueryWrapper<PersureHeartRateEntity> queryWrapper1 = new QueryWrapper<>();
//            LocalDate now = LocalDate.now();
//            queryWrapper1.eq("patient_uid", patientDoctorEntity.getPatientUid())
//                    .eq("upload_time", now);
//            List<PressureAnomalyEntity> anomalyRecords = pressureAnomalyMapper.selectList(queryWrapper1);
//
//            for(PressureAnomalyEntity anomalyRecord : anomalyRecords){
//                severe += anomalyRecord.getSevere();
//                moderate += anomalyRecord.getModerate();
//                mild += anomalyRecord.getMild();
//                elevated += anomalyRecord.getElevated();
//                low += anomalyRecord.getLow();
//                all += anomalyRecord.getAllNum();
//            }
//        }
//
//        int normal = all - severe - moderate - mild - elevated - low;
//        Map<String, Object> mapData = new LinkedHashMap<>();
//        mapData.put("重度", severe);
//        mapData.put("中度", moderate);
//        mapData.put("轻度", mild);
//        mapData.put("正常偏高", elevated);
//        mapData.put("正常", normal);
//        mapData.put("偏低", low);
//        mapData.put("累计人次", all);
//
//        JSONObject result = new JSONObject(mapData);
//        return result;
//    }
}