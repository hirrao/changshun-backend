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
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.*;
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
    public JSONObject getWeeklyPressureData(int weeksAgo, Long patientUid) {
        LocalDate date = LocalDate.now();
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
    public JSONObject getMonthlyPressureData(int monthsAgo, Long patientUid) {
        LocalDate date = LocalDate.now();
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
    public JSONObject getYearlyPressureData(int yearsAgo, Long patientUid) {
        LocalDate date = LocalDate.now();
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

    @Override
    public JSONObject getDailyAveragePressure(LocalDate date, Long patientUid) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        QueryWrapper<PersureHeartRateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_uid", patientUid)
                .between("upload_time", startOfDay, endOfDay);

        List<PersureHeartRateEntity> records = persureHeartRateMapper.selectList(queryWrapper);

        DoubleSummaryStatistics systolicStats = records.stream()
                .mapToDouble(PersureHeartRateEntity::getSystolic)
                .summaryStatistics();

        DoubleSummaryStatistics diastolicStats = records.stream()
                .mapToDouble(PersureHeartRateEntity::getDiastolic)
                .summaryStatistics();

        JSONObject result = new JSONObject();
        result.put("avg_systolic", systolicStats.getAverage());
        result.put("avg_diastolic", diastolicStats.getAverage());

        return result;
    }

    @Override
    public JSONObject getWeeklyAveragePressureByDay(Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = date.with(DayOfWeek.SUNDAY);

        JSONObject result = new JSONObject();

        for (LocalDate currentDate = startOfWeek; !currentDate.isAfter(endOfWeek); currentDate = currentDate.plusDays(1)) {
            JSONObject dailyAverage = getDailyAveragePressure(currentDate, patientUid);
            result.put(currentDate.toString(), dailyAverage);
        }

        return result;
    }

    @Override
    // 一个月的开头和结尾几天可能不是完整的一周，也算作一周
    public JSONObject getMonthlyAveragePressureByWeek(Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDate startOfMonth = date.withDayOfMonth(1);
        LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());

        JSONObject monthlyPressureData = new JSONObject();
        int weekCount = 1;

        LocalDate startOfWeek = startOfMonth;
        while (!startOfWeek.isAfter(endOfMonth)) {
            LocalDate endOfWeek = startOfWeek.with(DayOfWeek.SUNDAY);
            if (endOfWeek.isAfter(endOfMonth)) {
                endOfWeek = endOfMonth;
            }

            LocalDateTime startDateTime = startOfWeek.atStartOfDay();
            LocalDateTime endDateTime = endOfWeek.atTime(23, 59, 59);

            QueryWrapper<PersureHeartRateEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("patient_uid", patientUid)
                    .between("upload_time", startDateTime, endDateTime);

            List<PersureHeartRateEntity> weeklyRecords = persureHeartRateMapper.selectList(queryWrapper);

            DoubleSummaryStatistics systolicStats = weeklyRecords.stream()
                    .mapToDouble(PersureHeartRateEntity::getSystolic)
                    .summaryStatistics();

            DoubleSummaryStatistics diastolicStats = weeklyRecords.stream()
                    .mapToDouble(PersureHeartRateEntity::getDiastolic)
                    .summaryStatistics();

            JSONObject weeklyAverage = new JSONObject();
            weeklyAverage.put("avg_systolic", systolicStats.getAverage());
            weeklyAverage.put("avg_diastolic", diastolicStats.getAverage());

            monthlyPressureData.put("Week " + weekCount, weeklyAverage);

            // 下一周
            startOfWeek = endOfWeek.plusDays(1);
            weekCount++;
        }

        return monthlyPressureData;
    }

    @Override
    public JSONObject getYearlyAveragePressureByMonth(Long patientUid) {
        LocalDate date = LocalDate.now();
        JSONObject yearlyPressureData = new JSONObject();
        for (int month = 1; month <= 12; month++) {
            LocalDate startOfMonth = LocalDate.of(date.getYear(), month, 1);
            LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());

            LocalDateTime startDateTime = startOfMonth.atStartOfDay();
            LocalDateTime endDateTime = endOfMonth.atTime(23, 59, 59);

            QueryWrapper<PersureHeartRateEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("patient_uid", patientUid)
                    .between("upload_time", startDateTime, endDateTime);

            List<PersureHeartRateEntity> monthlyRecords = persureHeartRateMapper.selectList(queryWrapper);

            DoubleSummaryStatistics systolicStats = monthlyRecords.stream()
                    .mapToDouble(PersureHeartRateEntity::getSystolic)
                    .summaryStatistics();

            DoubleSummaryStatistics diastolicStats = monthlyRecords.stream()
                    .mapToDouble(PersureHeartRateEntity::getDiastolic)
                    .summaryStatistics();

            JSONObject monthlyAverage = new JSONObject();
            monthlyAverage.put("avg_systolic", systolicStats.getAverage());
            monthlyAverage.put("avg_diastolic", diastolicStats.getAverage());

            yearlyPressureData.put(startOfMonth.getMonth().toString(), monthlyAverage);
        }
        return yearlyPressureData;
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