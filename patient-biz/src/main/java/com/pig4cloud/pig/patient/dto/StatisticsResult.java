package com.pig4cloud.pig.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticsResult {

    private int 高血压家族遗传史;
    private int 吸烟史;
    private int 饮酒史;
    private int 传染病史;
    private int 食物过敏史;
    private double 高血压家族遗传史比例;
    private double 吸烟史比例;
    private double 饮酒史比例;
    private double 传染病史比例;
    private double 食物过敏史比例;

    /*private int earlyCvdFamilyHistoryCount;
    private double earlyCvdFamilyHistoryPercentage;
=======
    private int earlyCvdFamilyHistoryCount;

>>>>>>> origin/patient_service

    private int smokingCount;


    private int drinkingCount;


    private int infectiousDiseaseCount;


    private int foodAllergyCount;
<<<<<<< HEAD
    private double foodAllergyPercentage;*/

    /*private int earlyCvdFamilyHistoryCount; // 家族病史
    private int smokingCount;                // 吸烟人数
    private int drinkingCount;               // 饮酒人数
    private int infectiousDiseaseCount;      // 感染病史人数
    private int foodAllergyCount;            // 食物过敏人数*/
}
