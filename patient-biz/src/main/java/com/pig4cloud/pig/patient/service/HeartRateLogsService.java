package com.pig4cloud.pig.patient.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.HeartRateLogsEntity;

import java.time.LocalDate;

public interface HeartRateLogsService extends IService<HeartRateLogsEntity> {

    JSONArray getWeeklyHeartRateData(int weeksAgo, Long patientUid);
    JSONArray getMonthlyHeartRateData(int monthsAgo, Long patientUid);
    JSONArray getYearlyHeartRateData(int yearsAgo, Long patientUid);

    JSONObject getNewlyHeartRateData(Long patientUid);

    JSONObject getDailyAverageHeartRate(LocalDate date, Long patientUid);
    JSONArray getWeeklyAverageHeartRateByDay(int weeksAgo, Long patientUid);
    JSONArray getMonthlyAverageHeartRateByWeek(int monthsAgo, Long patientUid);
    JSONArray getYearlyAverageHeartRateByMonth(int yearsAgo, Long patientUid);
}