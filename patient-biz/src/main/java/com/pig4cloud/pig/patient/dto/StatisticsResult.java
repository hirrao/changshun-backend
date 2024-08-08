package com.pig4cloud.pig.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticsResult {
    private double earlyCvdFamilyHistoryPercentage;
    private double smokingPercentage;
    private double drinkingPercentage;
    private double infectiousDiseasePercentage;
    private double foodAllergyPercentage;
    /*private int earlyCvdFamilyHistoryCount; // 家族病史
    private int smokingCount;                // 吸烟人数
    private int drinkingCount;               // 饮酒人数
    private int infectiousDiseaseCount;      // 感染病史人数
    private int foodAllergyCount;            // 食物过敏人数*/
}
