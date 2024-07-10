package com.pig4cloud.pig.patient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.patient.dto.DiseasesCountDTO;
import com.pig4cloud.pig.patient.entity.AiPreDiagnosisEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AiPreDiagnosisMapper extends BaseMapper<AiPreDiagnosisEntity> {
    @Select("SELECT COUNT(*) FROM ai_pre_diagnosis " +
            "WHERE patient_uid IN " +
            "(SELECT patient_uid FROM patient_doctor WHERE doctor_uid = #{doctorUid} ) " +
            "AND (FIND_IN_SET(#{disease}, diseases_list) > 0)")
    int nocountPatientsWithDisease(@Param("doctorUid") Long doctorUid, @Param("disease") String disease);


    @Select("SELECT COUNT(*) FROM ai_pre_diagnosis " +
            "WHERE patient_uid IN " +
            "(SELECT patient_uid FROM patient_doctor WHERE doctor_uid = #{doctorUid} AND care = 1) " +
            "AND (FIND_IN_SET(#{disease}, diseases_list) > 0)")
    int countPatientsWithDisease(@Param("doctorUid") Long doctorUid, @Param("disease") String disease);


    int countPatientsWithHypertensionFamilyHistory(@Param("doctorUid") Long doctorUid);

    int countPatientsWithSmokingHistory(@Param("doctorUid") Long doctorUid);

    int countPatientsWithDrinkingHistory(@Param("doctorUid") Long doctorUid);

    int countPatientsWithInfectiousHistory(@Param("doctorUid") Long doctorUid);

    int countPatientsWithFoodAllergyHistory(@Param("doctorUid") Long doctorUid);

    int ccountPatientsWithHypertensionFamilyHistory(@Param("doctorUid") Long doctorUid);

    int ccountPatientsWithSmokingHistory(@Param("doctorUid") Long doctorUid);

    int ccountPatientsWithDrinkingHistory(@Param("doctorUid") Long doctorUid);

    int ccountPatientsWithInfectiousHistory(@Param("doctorUid") Long doctorUid);

    int ccountPatientsWithFoodAllergyHistory(@Param("doctorUid") Long doctorUid);

}