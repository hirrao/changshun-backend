package com.pig4cloud.pig.patient.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface PersureHeartRateService extends IService<PersureHeartRateEntity> {

    void addBloodPressure(PersureHeartRateEntity persureHeartRate);
    void AddPressureInBatches(List<PersureHeartRateEntity> result);
    String getWeekRangeByDate(LocalDate date);

    PersureHeartRateEntity setSdhAndRiskClass(PersureHeartRateEntity persureHeartRate);
    JSONObject classifyAllBloodPressure(Long patientUid);
    String getCurrentRiskAssessment(Long patientUid);
    String judgeRiskByBloodPressure(float systolic, float diastolic);
    PersureHeartRateEntity getTodayMaxBloodPressure(Long patientUid);
    PersureHeartRateEntity getTodayMinHeartRate(Long patientUid);
    JSONArray getWeeklyPressureData(int weeksAgo, Long patientUid);
    JSONArray getMonthlyPressureData(int monthsAgo, Long patientUid);
    JSONArray getYearlyPressureData(int yearsAgo, Long patientUid);

    JSONObject getDailyMaxMinAvgSystolic(LocalDate date, Long patientUid);
    JSONObject getWeeklyMaxMinAvgSystolic(LocalDate anyDateInWeek, Long patientUid);
    JSONObject getMonthlyMaxMinAvgSystolic(YearMonth yearMonth, Long patientUid);
    JSONObject getYearlyMaxMinAvgSystolic(Long patientUid);

    JSONObject getDailyMaxMinAvgDiastolic(LocalDate date, Long patientUid);
    JSONObject getWeeklyMaxMinAvgDiastolic(LocalDate anyDateInWeek, Long patientUid);
    JSONObject getMonthlyMaxMinAvgDiastolic(YearMonth yearMonth, Long patientUid);
    JSONObject getYearlyMaxMinAvgDiastolic(Long patientUid);

    JSONObject getDailyMaxMinAvgPressureDiff(LocalDate date, Long patientUid);
    JSONObject getWeeklyMaxMinAvgPressureDiff(LocalDate anyDateInWeek, Long patientUid);
    JSONObject getMonthlyMaxMinAvgPressureDiff(YearMonth yearMonth, Long patientUid);
    JSONObject getYearlyMaxMinAvgPressureDiff(Long patientUid);

     JSONObject getDailyAveragePressureHeartRate(LocalDate date, Long patientUid);
     JSONArray getWeeklyAveragePressureHeartRateByDay(LocalDate anyDateInWeek, Long patientUid);
     JSONArray getMonthlyAveragePressureHeartRateByWeek(YearMonth yearMonth, Long patientUid);
     JSONArray getYearlyAveragePressureHeartRateByMonth(int yearsAgo, Long patientUid);

    JSONArray getDailyConsecutiveAbnormalities(Long doctorUid);

    void updateSdhClassification(Long sdhId, Long patientUid);
    boolean savePersureHeartRate(PersureHeartRateEntity persureHeartRate);

    JSONObject getNewlyPressureData(Long patientUid);
    JSONObject getLatestMeasurementTime(Long patientUid);

    JSONObject countSdhClassificationByDoctorAndCare(Long doctorUid);

    JSONObject nocountSdhClassificationByDoctorAndCare(Long doctorUid);

    Map<String, Long> getDailyStatistics(Long doctorUid);

    JSONObject getLastSevenDayAnomalyNum(Long patientUid);

    JSONObject getRiskAssessmentNum(Long patientUid, LocalDate date);
    JSONObject getWeekAnomalyCount(Long patientUid, LocalDate anyDateInWeek);
    JSONObject getMonthAnomalyCount(Long patientUid, YearMonth yearMonth);

    JSONObject getAnomalyCountByDoctorUid(Long doctorUid, boolean care);

    JSONArray getPressureAndRiskByTimeRange(Long patientUid, LocalDate startTime, LocalDate endTime);
}