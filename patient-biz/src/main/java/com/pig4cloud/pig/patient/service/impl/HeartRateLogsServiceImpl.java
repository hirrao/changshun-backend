package com.pig4cloud.pig.patient.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.HeartRateLogsEntity;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import com.pig4cloud.pig.patient.mapper.HeartRateLogsMapper;
import com.pig4cloud.pig.patient.service.HeartRateLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 患者心率数据
 *
 * @author wangwenche
 * @date 2024-08-04 11:45:38
 */
@Service
public class HeartRateLogsServiceImpl extends ServiceImpl<HeartRateLogsMapper, HeartRateLogsEntity> implements HeartRateLogsService {
    @Autowired
    private HeartRateLogsMapper heartRateLogsMapper;

    @Override
    public JSONArray getWeeklyHeartRateData(int weeksAgo, Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDate startOfWeek = date.minusWeeks(weeksAgo + 1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        QueryWrapper<HeartRateLogsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("upload_time", startOfWeek.atStartOfDay(), endOfWeek.atTime(23, 59, 59))
                .eq("patient_uid", patientUid);

        List<HeartRateLogsEntity> weeklyRecords = heartRateLogsMapper.selectList(queryWrapper);

        Map<LocalDate, List<HeartRateLogsEntity>> recordsByDate = weeklyRecords.stream()
                .collect(Collectors.groupingBy(record -> record.getUploadTime().toLocalDate()));

        Map<LocalDate, JSONObject> sortedDailyData = new TreeMap<>();

        for (Map.Entry<LocalDate, List<HeartRateLogsEntity>> entry : recordsByDate.entrySet()) {
            LocalDate recordDate = entry.getKey();
            JSONArray heartRateArray = new JSONArray();

            for (HeartRateLogsEntity record : entry.getValue()) {
                heartRateArray.add(record.getHeartRate() != null ? record.getHeartRate() : null);
            }

            JSONObject dailyData = new JSONObject();
            dailyData.put("heartRate", heartRateArray);
            dailyData.put("date", recordDate);

            sortedDailyData.put(recordDate, dailyData);
        }

        // 确保一周的每天都有数据，即使其中某天没有血压和心率数据
        for (LocalDate day = startOfWeek; !day.isAfter(endOfWeek); day = day.plusDays(1)) {
            if (!sortedDailyData.containsKey(day)) {
                JSONObject emptyData = new JSONObject();
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
    public JSONArray getMonthlyHeartRateData(int monthsAgo, Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDate startOfMonth = date.minusMonths(monthsAgo).withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());

        QueryWrapper<HeartRateLogsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("upload_time", startOfMonth.atStartOfDay(), endOfMonth.atTime(23, 59, 59))
                .eq("patient_uid", patientUid);

        List<HeartRateLogsEntity> monthlyRecords = heartRateLogsMapper.selectList(queryWrapper);

        Map<LocalDate, List<HeartRateLogsEntity>> recordsByDate = monthlyRecords.stream()
                .collect(Collectors.groupingBy(record -> record.getUploadTime().toLocalDate()));

        Map<LocalDate, JSONObject> sortedDailyData = new TreeMap<>();

        for (Map.Entry<LocalDate, List<HeartRateLogsEntity>> entry : recordsByDate.entrySet()) {
            LocalDate recordDate = entry.getKey();
            JSONArray heartRateArray = new JSONArray();

            for (HeartRateLogsEntity record : entry.getValue()) {
                heartRateArray.add(record.getHeartRate() != null ? record.getHeartRate() : null);
            }

            JSONObject dailyData = new JSONObject();
            dailyData.put("heart rate", heartRateArray);
            dailyData.put("date", recordDate);

            sortedDailyData.put(recordDate, dailyData);
        }

        for (LocalDate day = startOfMonth; !day.isAfter(endOfMonth); day = day.plusDays(1)) {
            if (!sortedDailyData.containsKey(day)) {
                JSONObject emptyData = new JSONObject();
                emptyData.put("heart rate", new JSONArray());
                emptyData.put("date", day);
                sortedDailyData.put(day, emptyData);
            }
        }

        JSONArray pressureData = new JSONArray();
        pressureData.addAll(sortedDailyData.values());

        return pressureData;
    }

    @Override
    public JSONArray getYearlyHeartRateData(int yearsAgo, Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDate startOfYear = date.minusYears(yearsAgo).withDayOfYear(1);
        LocalDate endOfYear = startOfYear.with(TemporalAdjusters.lastDayOfYear());

        QueryWrapper<HeartRateLogsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("upload_time", startOfYear.atStartOfDay(), endOfYear.atTime(23, 59, 59))
                .eq("patient_uid", patientUid);

        List<HeartRateLogsEntity> yearlyRecords = heartRateLogsMapper.selectList(queryWrapper);

        Map<LocalDate, List<HeartRateLogsEntity>> recordsByDate = yearlyRecords.stream()
                .collect(Collectors.groupingBy(record -> record.getUploadTime().toLocalDate()));

        Map<LocalDate, JSONObject> sortedDailyData = new TreeMap<>();

        for (Map.Entry<LocalDate, List<HeartRateLogsEntity>> entry : recordsByDate.entrySet()) {
            LocalDate recordDate = entry.getKey();
            JSONArray heartRateArray = new JSONArray();

            for (HeartRateLogsEntity record : entry.getValue()) {
                heartRateArray.add(record.getHeartRate() != null ? record.getHeartRate() : null);
            }

            JSONObject dailyData = new JSONObject();
            dailyData.put("heartRate", heartRateArray);
            dailyData.put("date", recordDate);

            sortedDailyData.put(recordDate, dailyData);
        }

        for (LocalDate day = startOfYear; !day.isAfter(endOfYear); day = day.plusDays(1)) {
            if (!sortedDailyData.containsKey(day)) {
                JSONObject emptyData = new JSONObject();
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
    public JSONObject getNewlyPressureData(Long patientUid) {
        HeartRateLogsEntity measure =  heartRateLogsMapper.getLatestMeasurement(patientUid);
        JSONObject data = new JSONObject();
        data.put("心率", measure.getHeartRate());
        data.put("时间", measure.getUploadTime());
        return data;
    }
}