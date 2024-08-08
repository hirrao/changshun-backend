package com.pig4cloud.pig.patient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.dto.StatisticsResult;
import com.pig4cloud.pig.patient.entity.AiPreDiagnosisEntity;

import java.util.Map;

public interface AiPreDiagnosisService extends IService<AiPreDiagnosisEntity> {
    R<Map<String, Integer>> getPatientDiseasesCount(Long doctorUid);
    R<Map<String, Integer>> getCarePatientDiseasesCount(Long doctorUid);

    StatisticsResult getStatisticsByDoctor(Long doctorUid);
    StatisticsResult getCareStatisticsByDoctor(Long doctorUid);

    String generateAiPreDiagnosisReport(Long patientUid);
    String getSmokingAndDrinkingInfo(Long patientUid);
    String getDiagnosisDetails(Long patientUid);



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