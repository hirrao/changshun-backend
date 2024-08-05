package com.pig4cloud.pig.patient.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.dto.DiseasesCountDTO;
import com.pig4cloud.pig.patient.entity.AiPreDiagnosisEntity;

import java.util.Map;

public interface AiPreDiagnosisService extends IService<AiPreDiagnosisEntity> {
    Map<String, Integer> getDiseasesStatistics(Long doctorUid);

    /*Map<String, Integer> nocountPatientsWithDiseases(Long doctorUid);

    Map<String, Integer> countPatientsWithDiseases(Long doctorUid);

    int countPatientsWithHypertensionFamilyHistory(Long doctorUid);

    int countPatientsWithSmokingHistory(Long doctorUid);

    int countPatientsWithDrinkingHistory(Long doctorUid);

    int countPatientsWithInfectiousHistory(Long doctorUid);

    int countPatientsWithFoodAllergyHistory(Long doctorUid);

    int ccountPatientsWithHypertensionFamilyHistory(Long doctorUid);

    int ccountPatientsWithSmokingHistory(Long doctorUid);

    int ccountPatientsWithDrinkingHistory(Long doctorUid);

    int ccountPatientsWithInfectiousHistory(Long doctorUid);

    int ccountPatientsWithFoodAllergyHistory(Long doctorUid);*/

    boolean saveAiPreDiagnosis(AiPreDiagnosisEntity aiPreDiagnosis);

    boolean updateAiPreDiagnosis(AiPreDiagnosisEntity aiPreDiagnosis);

    Boolean calculateIsClinical(AiPreDiagnosisEntity aiPreDiagnosis);

    /*JSONObject ccountPatientsHistory(Long doctorUid);
    JSONObject countPatientsHistory(Long doctorUid);*/
}