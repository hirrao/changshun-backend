package com.pig4cloud.pig.patient.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.HeartRateLogsEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface HeartRateLogsService extends IService<HeartRateLogsEntity> {

    void addHeartRate(Long patientUid, LocalDateTime time, int heartRate);

    JSONArray getWeeklyHeartRateData(int weeksAgo, Long patientUid);
    JSONArray getMonthlyHeartRateData(int monthsAgo, Long patientUid);
    JSONArray getYearlyHeartRateData(int yearsAgo, Long patientUid);

    JSONObject getNewlyHeartRateData(Long patientUid);

    JSONObject getDailyAverageHeartRate(LocalDate date, Long patientUid);
    JSONArray getWeeklyAverageHeartRateByDay(LocalDate anyDateInWeek, Long patientUid);
    JSONArray getMonthlyAverageHeartRateByWeek(int monthsAgo, Long patientUid);
    JSONArray getYearlyAverageHeartRateByMonth(int yearsAgo, Long patientUid);

    JSONArray getDailyConsecutiveAbnormalities(Long doctorUid);

     int countPatientsWithLowHeartRate(Long doctorUid);

     int countPatientsWithNormalHeartRate(Long doctorUid);

     int countPatientsWithHighHeartRate(Long doctorUid);

     int ccountPatientsWithLowHeartRate(Long doctorUid);

     int ccountPatientsWithNormalHeartRate(Long doctorUid);

     int ccountPatientsWithHighHeartRate(Long doctorUid);


    JSONObject getHeartRateStatistics(Long doctorUid);
    JSONObject getcareHeartRateStatistics(Long doctorUid);
}