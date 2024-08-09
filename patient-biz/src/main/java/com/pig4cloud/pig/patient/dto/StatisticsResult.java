package com.pig4cloud.pig.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticsResult {
    private int earlyCvdFamilyHistoryCount;
    private double earlyCvdFamilyHistoryPercentage;

    private int smokingCount;
    private double smokingPercentage;

    private int drinkingCount;
    private double drinkingPercentage;

    private int infectiousDiseaseCount;
    private double infectiousDiseasePercentage;

    private int foodAllergyCount;
    private double foodAllergyPercentage;
    /*private int earlyCvdFamilyHistoryCount; // 家族病史
    private int smokingCount;                // 吸烟人数
    private int drinkingCount;               // 饮酒人数
    private int infectiousDiseaseCount;      // 感染病史人数
    private int foodAllergyCount;            // 食物过敏人数*/
}
