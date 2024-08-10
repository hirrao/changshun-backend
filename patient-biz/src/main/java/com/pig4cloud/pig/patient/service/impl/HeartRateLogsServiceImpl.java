package com.pig4cloud.pig.patient.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.HeartRateLogsEntity;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import com.pig4cloud.pig.patient.mapper.HeartRateLogsMapper;
import com.pig4cloud.pig.patient.mapper.PatientBaseMapper;
import com.pig4cloud.pig.patient.mapper.PatientDoctorMapper;
import com.pig4cloud.pig.patient.service.HeartRateLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
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
    @Autowired
    private PatientDoctorMapper patientDoctorMapper;
    @Autowired
    private PatientBaseMapper patientBaseMapper;

    @Override
    public void addHeartRate(Long patientUid, LocalDateTime time, int heartRate) {
        HeartRateLogsEntity heartRateLogs = new HeartRateLogsEntity();
        heartRateLogs.setHeartRate(heartRate);
        heartRateLogs.setPatientUid(patientUid);
        if(time == null) {
            heartRateLogs.setUploadTime(LocalDateTime.now());
        } else {
            heartRateLogs.setUploadTime(time);
        }

        heartRateLogsMapper.insert(heartRateLogs);
    }

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
    public JSONObject getNewlyHeartRateData(Long patientUid) {
        HeartRateLogsEntity measure =  heartRateLogsMapper.getLatestMeasurement(patientUid);
        JSONObject data = new JSONObject();
        data.put("心率", measure.getHeartRate());
        data.put("时间", measure.getUploadTime());
        return data;
    }

    @Override
    public JSONObject getDailyAverageHeartRate(LocalDate date, Long patientUid) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        QueryWrapper<HeartRateLogsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_uid", patientUid)
                .between("upload_time", startOfDay, endOfDay);

        List<HeartRateLogsEntity> records = heartRateLogsMapper.selectList(queryWrapper);
        JSONObject result = new JSONObject();

        if (records.isEmpty()) {
            result.put("avg_heart_rate", null);
        } else {
            DoubleSummaryStatistics heartRateStats = records.stream()
                    .mapToDouble(HeartRateLogsEntity::getHeartRate)
                    .summaryStatistics();

            result.put("avg_heart_rate", heartRateStats.getCount() > 0 ? heartRateStats.getAverage() : null);
        }

        return result;
    }

    @Override
    public JSONArray getWeeklyAverageHeartRateByDay(LocalDate anyDateInWeek, Long patientUid) {
        // 计算给定日期所在的星期的开始和结束日期
        LocalDate startOfWeek = anyDateInWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        JSONArray result = new JSONArray();

        for (LocalDate currentDate = startOfWeek; !currentDate.isAfter(endOfWeek); currentDate = currentDate.plusDays(1)) {
            JSONObject dailyAverage = getDailyAverageHeartRate(currentDate, patientUid);
            dailyAverage.put("date", currentDate);

            result.add(dailyAverage);
        }

        return result;
    }

    @Override
    // 一个月的开头和结尾几天可能不是完整的一周，也算作一周
    public JSONArray getMonthlyAverageHeartRateByWeek(YearMonth yearMonth, Long patientUid) {
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        JSONArray monthlyPressureData = new JSONArray();

        LocalDate startOfWeek = startOfMonth;
        while (!startOfWeek.isAfter(endOfMonth)) {
            LocalDate endOfWeek = startOfWeek.with(DayOfWeek.SUNDAY);
            if (endOfWeek.isAfter(endOfMonth)) {
                endOfWeek = endOfMonth;
            }

            LocalDateTime startDateTime = startOfWeek.atStartOfDay();
            LocalDateTime endDateTime = endOfWeek.atTime(23, 59, 59);

            QueryWrapper<HeartRateLogsEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("patient_uid", patientUid)
                    .between("upload_time", startDateTime, endDateTime);

            List<HeartRateLogsEntity> weeklyRecords = heartRateLogsMapper.selectList(queryWrapper);

            DoubleSummaryStatistics heartRateStats = weeklyRecords.stream()
                    .mapToDouble(HeartRateLogsEntity::getHeartRate)
                    .summaryStatistics();

            JSONObject weeklyAverage = new JSONObject();
            weeklyAverage.put("start_date", startOfWeek.toString());
            weeklyAverage.put("end_date", endOfWeek.toString());
            weeklyAverage.put("avg_heart_rate", heartRateStats.getCount() > 0 ? heartRateStats.getAverage() : null);
            monthlyPressureData.add(weeklyAverage);

            // 下一周
            startOfWeek = endOfWeek.plusDays(1);
        }

        return monthlyPressureData;
    }

    @Override
    public JSONArray getYearlyAverageHeartRateByMonth(int yearsAgo, Long patientUid) {
        LocalDate date = LocalDate.now();
        LocalDate startOfYear = date.minusYears(yearsAgo).withDayOfYear(1);
        int year = startOfYear.getYear();

        JSONArray yearlyPressureData = new JSONArray();
        for (int month = 1; month <= 12; month++) {
            LocalDate startOfMonth = LocalDate.of(year, month, 1);
            LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());

            LocalDateTime startDateTime = startOfMonth.atStartOfDay();
            LocalDateTime endDateTime = endOfMonth.atTime(23, 59, 59);

            QueryWrapper<HeartRateLogsEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("patient_uid", patientUid)
                    .between("upload_time", startDateTime, endDateTime);

            List<HeartRateLogsEntity> monthlyRecords = heartRateLogsMapper.selectList(queryWrapper);

            DoubleSummaryStatistics heartRateStats = monthlyRecords.stream()
                    .mapToDouble(HeartRateLogsEntity::getHeartRate)
                    .summaryStatistics();

            JSONObject monthlyAverage = new JSONObject();
            monthlyAverage.put("month", startOfMonth.getMonth().toString());
            monthlyAverage.put("avg_heart_rate", heartRateStats.getCount() > 0 ? heartRateStats.getAverage() : null);

            yearlyPressureData.add(monthlyAverage);
        }
        return yearlyPressureData;
    }

    @Override
    public JSONArray getDailyConsecutiveAbnormalities(Long doctorUid) {
        LocalDate date = LocalDate.now();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        QueryWrapper<PatientDoctorEntity> doctorQueryWrapper = new QueryWrapper<>();
        doctorQueryWrapper.eq("doctor_uid", doctorUid);

        List<Long> patientUids = patientDoctorMapper.selectList(doctorQueryWrapper)
                .stream()
                .map(PatientDoctorEntity::getPatientUid)
                .toList();

        if (patientUids.isEmpty()) {
            return new JSONArray();
        }

        // 根据患者UID列表查询心率记录
        QueryWrapper<HeartRateLogsEntity> heartRateQueryWrapper = new QueryWrapper<>();
        heartRateQueryWrapper.between("upload_time", startOfDay, endOfDay)
                .in("patient_uid", patientUids);



        List<HeartRateLogsEntity> todayRecords = heartRateLogsMapper.selectList(heartRateQueryWrapper);

        // 建立patient_uid和PatientBaseEntity之间的映射，便于后续查询患者基本信息
        Map<Long, PatientBaseEntity> patientBaseMap = patientBaseMapper.selectList(new QueryWrapper<>())
                .stream()
                .collect(Collectors.toMap(PatientBaseEntity::getPatientUid, Function.identity()));

        // 建立patient_uid和HeartRateLogsEntity之间的映射，便于后续按患者统计心率血压异常次数
        Map<Long, List<HeartRateLogsEntity>> recordsByPatient = todayRecords.stream()
                .collect(Collectors.groupingBy(HeartRateLogsEntity::getPatientUid));

        JSONArray result = new JSONArray();

        for (Map.Entry<Long, List<HeartRateLogsEntity>> entry : recordsByPatient.entrySet()) {
            Long patientUid = entry.getKey();
            List<HeartRateLogsEntity> records = entry.getValue();
            records.sort(Comparator.comparing(HeartRateLogsEntity::getUploadTime));

            List<JSONObject> patientDataList = new ArrayList<>();

            int consecutiveLowHr = 0;
            LocalDateTime lowHrStart = null;
            LocalDateTime lowHrEnd = null;

            for (HeartRateLogsEntity record : records) {
                if (record.getHeartRate() < 60) {
                    if (consecutiveLowHr == 0) {
                        lowHrStart = record.getUploadTime();
                    }
                    consecutiveLowHr++;
                    lowHrEnd = record.getUploadTime();
                } else {
                    if (consecutiveLowHr > 1) {
                        JSONObject patientData = new JSONObject();
                        PatientBaseEntity patientBase = patientBaseMap.get(patientUid);
                        LocalDate birthday = patientBase.getBirthday();
                        LocalDate current = LocalDate.now();
                        int age = 0;
                        if (birthday != null) {
                            age = Period.between(birthday, current).getYears();
                        }

                        patientData.put("name", patientBase.getPatientName());
                        patientData.put("sex", patientBase.getSex());
                        patientData.put("age", age);
                        patientData.put("abnormality", "心率低于60次/分钟");
                        patientData.put("ill", "心率过低");
                        patientData.put("count", consecutiveLowHr);
                        patientData.put("duration", String.format("%d小时%d分钟",
                                Duration.between(lowHrStart, lowHrEnd).toHours(),
                                Duration.between(lowHrStart, lowHrEnd).toMinutes() % 60));

                        patientDataList.add(patientData);
                    }
                    consecutiveLowHr = 0;
                }
            }

            // 处理最后一条记录后的未记录异常情况
            if (consecutiveLowHr > 1) {
                JSONObject patientData = new JSONObject();
                PatientBaseEntity patientBase = patientBaseMap.get(patientUid);
                LocalDate birthday = patientBase.getBirthday();
                LocalDate current = LocalDate.now();
                int age = 0;
                if (birthday != null) {
                    age = Period.between(birthday, current).getYears();
                }

                patientData.put("name", patientBase.getPatientName());
                patientData.put("sex", patientBase.getSex());
                patientData.put("age", age);
                patientData.put("abnormality", "心率低于60次/分钟");
                patientData.put("ill", "心率过低");
                patientData.put("count", consecutiveLowHr);
                patientData.put("duration", String.format("%d小时%d分钟",
                        Duration.between(lowHrStart, lowHrEnd).toHours(),
                        Duration.between(lowHrStart, lowHrEnd).toMinutes() % 60));

                patientDataList.add(patientData);
            }

            result.addAll(patientDataList);
        }

        result.sort((a, b) -> ((Integer) ((JSONObject) b).get("count")).compareTo((Integer) ((JSONObject) a).get("count")));
        return result;
    }

    @Override
    public int countPatientsWithLowHeartRate(Long doctorUid) {
        return heartRateLogsMapper.countPatientsWithLowHeartRate(doctorUid);
    }

    @Override
    public int countPatientsWithNormalHeartRate(Long doctorUid) {
        return heartRateLogsMapper.countPatientsWithNormalHeartRate(doctorUid);
    }

    @Override
    public int countPatientsWithHighHeartRate(Long doctorUid) {
        return heartRateLogsMapper.countPatientsWithHighHeartRate(doctorUid);
    }

    @Override
    public int ccountPatientsWithLowHeartRate(Long doctorUid) {
        return heartRateLogsMapper.ccountPatientsWithLowHeartRate(doctorUid);
    }

    @Override
    public int ccountPatientsWithNormalHeartRate(Long doctorUid) {
        return heartRateLogsMapper.ccountPatientsWithNormalHeartRate(doctorUid);
    }

    @Override
    public int ccountPatientsWithHighHeartRate(Long doctorUid) {
        return heartRateLogsMapper.ccountPatientsWithHighHeartRate(doctorUid);
    }


    @Override
    public JSONObject getHeartRateStatistics(Long doctorUid) {
        int normal = 0, low = 0, high = 0, all = 0;
        normal = countPatientsWithNormalHeartRate(doctorUid);
        low = countPatientsWithLowHeartRate(doctorUid);
        high = countPatientsWithHighHeartRate(doctorUid);
        all = normal + low + high;

        Map<String, Object> mapData = new LinkedHashMap<>();
        mapData.put("正常", normal);
        mapData.put("过缓", low);
        mapData.put("过急", high);
        mapData.put("累计", all);

        JSONObject result = new JSONObject(mapData);
        return result;
    }

    @Override
    public JSONObject getcareHeartRateStatistics(Long doctorUid) {
        int normal = 0, low = 0, high = 0, all = 0;
        normal = ccountPatientsWithNormalHeartRate(doctorUid);
        low = ccountPatientsWithLowHeartRate(doctorUid);
        high = ccountPatientsWithHighHeartRate(doctorUid);
        all = normal + low + high;

        Map<String, Object> mapData = new LinkedHashMap<>();
        mapData.put("正常", normal);
        mapData.put("过缓", low);
        mapData.put("过急", high);
        mapData.put("累计", all);

        JSONObject result = new JSONObject(mapData);
        return result;
    }
}