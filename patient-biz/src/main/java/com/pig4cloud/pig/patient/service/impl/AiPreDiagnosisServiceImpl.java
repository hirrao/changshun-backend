package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.dto.DiseasesCountDTO;
import com.pig4cloud.pig.patient.entity.AiPreDiagnosisEntity;
import com.pig4cloud.pig.patient.mapper.AiPreDiagnosisMapper;
import com.pig4cloud.pig.patient.service.AiPreDiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public int nocountPatientsWithDiseases(Long doctorUid) {
        return aiPreDiagnosisMapper.nocountPatientsWithDiseases(doctorUid);
    }

    @Override
    public int countPatientsWithDiseases(Long doctorUid) {
        return aiPreDiagnosisMapper.countPatientsWithDiseases(doctorUid);
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
        return aiPreDiagnosisMapper.countPatientsWithHypertensionFamilyHistory(doctorUid);
    }

    @Override
    public int ccountPatientsWithSmokingHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.countPatientsWithSmokingHistory(doctorUid);
    }

    @Override
    public int ccountPatientsWithDrinkingHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.countPatientsWithDrinkingHistory(doctorUid);
    }

    @Override
    public int ccountPatientsWithInfectiousHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.countPatientsWithInfectiousHistory(doctorUid);
    }

    @Override
    public int ccountPatientsWithFoodAllergyHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.countPatientsWithFoodAllergyHistory(doctorUid);
    }
}