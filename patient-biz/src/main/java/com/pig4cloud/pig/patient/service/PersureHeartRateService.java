package com.pig4cloud.pig.patient.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PersureHeartRateService extends IService<PersureHeartRateEntity> {
    JSONObject classifyAllBloodPressure(Long patientUid);
    String getCurrentRiskAssessment(Long patientUid);
    String judgeRiskByBloodPressure(float systolic, float diastolic);
    PersureHeartRateEntity getTodayMaxBloodPressure(Long patientUid);
    PersureHeartRateEntity getTodayMinHeartRate(Long patientUid);
    JSONArray getWeeklyPressureHeartRateData(int weeksAgo, Long patientUid);
    JSONArray getMonthlyPressureHeartRateData(int monthsAgo, Long patientUid);
    JSONArray getYearlyPressureHeartRateData(int yearsAgo, Long patientUid);

    JSONObject getDailyMaxMinAvgSystolic(Long patientUid);
    JSONObject getWeeklyMaxMinAvgSystolic(Long patientUid);
    JSONObject getMonthlyMaxMinAvgSystolic(Long patientUid);
    JSONObject getYearlyMaxMinAvgSystolic(Long patientUid);

    JSONObject getDailyMaxMinAvgDiastolic(Long patientUid);
    JSONObject getWeeklyMaxMinAvgDiastolic(Long patientUid);
    JSONObject getMonthlyMaxMinAvgDiastolic(Long patientUid);
    JSONObject getYearlyMaxMinAvgDiastolic(Long patientUid);

    JSONObject getDailyMaxMinAvgPressureDiff(Long patientUid);
    JSONObject getWeeklyMaxMinAvgPressureDiff(Long patientUid);
    JSONObject getMonthlyMaxMinAvgPressureDiff(Long patientUid);
    JSONObject getYearlyMaxMinAvgPressureDiff(Long patientUid);

    // JSONObject getDailyAveragePressureHeartRate(LocalDate date, Long patientUid);
    // JSONArray getWeeklyAveragePressureHeartRateByDay(int weeksAgo, Long patientUid);
    // JSONArray getMonthlyAveragePressureHeartRateByWeek(int monthsAgo, Long patientUid);
    // JSONArray getYearlyAveragePressureHeartRateByMonth(int yearsAgo, Long patientUid);

    // JSONArray getDailyConsecutiveAbnormalities(Long doctorUid);

    // int countPatientsWithLowHeartRate(Long doctorUid);

    // int countPatientsWithNormalHeartRate(Long doctorUid);

    // int countPatientsWithHighHeartRate(Long doctorUid);

     // int ccountPatientsWithLowHeartRate(Long doctorUid);

     // int ccountPatientsWithNormalHeartRate(Long doctorUid);

     // int ccountPatientsWithHighHeartRate(Long doctorUid);

    void updateSdhClassification(Long sdhId, Long patientUid);
     boolean savePersureHeartRate(PersureHeartRateEntity persureHeartRate);

    JSONObject getNewlyPressureHeartRateData(Long patientUid);


    JSONObject countSdhClassificationByDoctorAndCare(Long doctorUid);

    JSONObject nocountSdhClassificationByDoctorAndCare(Long doctorUid);

    // JSONObject getHeartRateStatistics(Long doctorUid);
    // JSONObject getcareHeartRateStatistics(Long doctorUid);

    Map<String, Long> getDailyStatistics(Long doctorUid);

    JSONObject getRiskAssessmentNum(Long patientUid, LocalDate date);
    JSONObject getLastSevenDayAnomalyNum(Long patientUid);
    JSONObject getWeekAnomalyCount(Long patientUid, int weeksAgo);
    // JSONObject getAnomalyCountByDoctorUid(Long doctorUid, boolean care);
}