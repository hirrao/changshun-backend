package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.dto.DiseasesCountDTO;
import com.pig4cloud.pig.patient.entity.AiPreDiagnosisEntity;
import com.pig4cloud.pig.patient.mapper.AiPreDiagnosisMapper;
import com.pig4cloud.pig.patient.service.AiPreDiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI预问诊
 *
 * @author wangwenche
 * @date 2024-07-07 11:55:10
 */
@Service
public class AiPreDiagnosisServiceImpl extends ServiceImpl<AiPreDiagnosisMapper, AiPreDiagnosisEntity> implements AiPreDiagnosisService {

    private final AiPreDiagnosisMapper aiPreDiagnosisMapper;

    @Autowired
    public AiPreDiagnosisServiceImpl(AiPreDiagnosisMapper aiPreDiagnosisMapper) {
        this.aiPreDiagnosisMapper = aiPreDiagnosisMapper;
    }

    @Override
    public Map<String, Integer> nocountPatientsWithDiseases(Long doctorUid) {
        List<String> diseaseNames = Arrays.asList("血脂异常", "脑血管病", "心脏疾病", "肾脏疾病",
                "周围血管病", "视网膜病变", "糖尿病", "其他");

        Map<String, Integer> diseaseCounts = new HashMap<>();
        for (String disease : diseaseNames) {
            int count = aiPreDiagnosisMapper.nocountPatientsWithDisease(doctorUid, disease);
            diseaseCounts.put(disease, count);
        }

        return diseaseCounts;
    }

    @Override
    public Map<String, Integer> countPatientsWithDiseases(Long doctorUid) {
        List<String> diseaseNames = Arrays.asList("血脂异常", "脑血管病", "心脏疾病", "肾脏疾病",
                "周围血管病", "视网膜病变", "糖尿病", "其他");

        Map<String, Integer> diseaseCounts = new HashMap<>();
        for (String disease : diseaseNames) {
            int count = aiPreDiagnosisMapper.countPatientsWithDisease(doctorUid, disease);
            diseaseCounts.put(disease, count);
        }

        return diseaseCounts;
    }

    @Override
    public int countPatientsWithHypertensionFamilyHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.countPatientsWithHypertensionFamilyHistory(doctorUid);
    }

    @Override
    public int countPatientsWithSmokingHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.countPatientsWithSmokingHistory(doctorUid);
    }

    @Override
    public int countPatientsWithDrinkingHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.countPatientsWithDrinkingHistory(doctorUid);
    }

    @Override
    public int countPatientsWithInfectiousHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.countPatientsWithInfectiousHistory(doctorUid);
    }

    @Override
    public int countPatientsWithFoodAllergyHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.countPatientsWithFoodAllergyHistory(doctorUid);
    }


    @Override
    public int ccountPatientsWithHypertensionFamilyHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.ccountPatientsWithHypertensionFamilyHistory(doctorUid);
    }

    @Override
    public int ccountPatientsWithSmokingHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.ccountPatientsWithSmokingHistory(doctorUid);
    }

    @Override
    public int ccountPatientsWithDrinkingHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.ccountPatientsWithDrinkingHistory(doctorUid);
    }

    @Override
    public int ccountPatientsWithInfectiousHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.ccountPatientsWithInfectiousHistory(doctorUid);
    }

    @Override
    public int ccountPatientsWithFoodAllergyHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.ccountPatientsWithFoodAllergyHistory(doctorUid);
    }

    @Override
    public boolean saveAiPreDiagnosis(AiPreDiagnosisEntity aiPreDiagnosis) {
        int riskFactorsCount = calculateRiskFactors(aiPreDiagnosis);
        aiPreDiagnosis.setDangerReason(riskFactorsCount);
        return saveOrUpdate(aiPreDiagnosis);
    }

    @Override
    public boolean updateAiPreDiagnosis(AiPreDiagnosisEntity aiPreDiagnosis) {
        int riskFactorsCount = calculateRiskFactors(aiPreDiagnosis);
        aiPreDiagnosis.setDangerReason(riskFactorsCount);
        return updateById(aiPreDiagnosis);
    }

    private int calculateRiskFactors(AiPreDiagnosisEntity aiPreDiagnosis) {
        int riskFactorsCount = 0;

        // 根据你的危险因素定义，实现具体逻辑
        if (isMaleOver55(aiPreDiagnosis) || isFemaleOver65(aiPreDiagnosis)) {
            riskFactorsCount++;
        }
        if ("是".equals(aiPreDiagnosis.getSmokingStatus())) {
            riskFactorsCount++;
        }
        if (aiPreDiagnosis.getEarlyCvdFamilyHistory() == 1) {
            riskFactorsCount++;
        }
        // 判断是否有血脂异常
        if (hasDiseasesList(aiPreDiagnosis.getDiseasesList(), "血脂异常")) {
            riskFactorsCount++;
        }

        return riskFactorsCount;
    }

    private boolean isMaleOver55(AiPreDiagnosisEntity aiPreDiagnosis) {
        return "男性".equals(aiPreDiagnosis.getGender()) && calculateAge(aiPreDiagnosis.getBirthday()) > 55;
    }

    private boolean isFemaleOver65(AiPreDiagnosisEntity aiPreDiagnosis) {
        return "女性".equals(aiPreDiagnosis.getGender()) && calculateAge(aiPreDiagnosis.getBirthday()) > 65;
    }

    private int calculateAge(LocalDate birthday) {
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    /**
     * 判断患病列表中是否包含特定的疾病
     * @param diseasesList 患病列表，逗号分隔的字符串
     * @param diseaseName 疾病名称
     * @return 是否包含
     */
    private boolean hasDiseasesList(String diseasesList, String diseaseName) {
        if (diseasesList == null || diseasesList.isEmpty()) {
            return false;
        }
        // 将患病列表按逗号分隔成数组
        String[] diseases = diseasesList.split(",");
        for (String disease : diseases) {
            // 去除空格后进行比较
            if (disease.trim().equals(diseaseName)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public Boolean calculateIsClinical(AiPreDiagnosisEntity aiPreDiagnosis) {
        if (aiPreDiagnosis.getDiseasesList() != null) {
            String diseasesList = aiPreDiagnosis.getDiseasesList().toLowerCase(); // 转为小写便于比较
            // 判断是否包含以下关键词
            boolean hasClinicalDisease = diseasesList.contains("脑血管病") ||
                    diseasesList.contains("心脏疾病") ||
                    diseasesList.contains("肾脏疾病") ||
                    diseasesList.contains("周围血管病") ||
                    diseasesList.contains("视网膜病变") ||
                    diseasesList.contains("糖尿病");
            return hasClinicalDisease;
        }
        return false;
    }

    @Override
    public boolean saveOrUpdate(AiPreDiagnosisEntity entity) {
        boolean success = super.saveOrUpdate(entity);
        // 更新 isClinical 字段
        entity.setIsClinical(calculateIsClinical(entity));
        super.updateById(entity);
        return success;
    }
}