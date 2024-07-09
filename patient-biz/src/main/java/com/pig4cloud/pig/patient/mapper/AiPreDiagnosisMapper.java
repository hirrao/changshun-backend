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
            "WHERE patientUid IN " +
            "(SELECT patientUid FROM patient_doctor WHERE doctor_uid = #{doctor_uid}) " +
            "AND (" +
            "FIND_IN_SET('血脂异常', diseasesList) > 0 OR " +
            "FIND_IN_SET('脑血管病', diseasesList) > 0 OR " +
            "FIND_IN_SET('心脏疾病', diseasesList) > 0 OR " +
            "FIND_IN_SET('肾脏疾病', diseasesList) > 0 OR " +
            "FIND_IN_SET('周围血管病', diseasesList) > 0 OR " +
            "FIND_IN_SET('视网膜病变', diseasesList) > 0 OR " +
            "FIND_IN_SET('糖尿病', diseasesList) > 0 OR " +
            "diseasesList LIKE '%其他%' " +
            ")")
    int nocountPatientsWithDiseases(@Param("doctorUid") Long doctorUid);


    @Select("SELECT COUNT(*) FROM ai_pre_diagnosis " +
            "WHERE patientUid IN " +
            "(SELECT patientUid FROM patient_doctor WHERE doctorUid = #{doctorUid} AND care = 1) " +
            "AND (FIND_IN_SET('血脂异常', diseasesList) > 0 " +
            "OR FIND_IN_SET('脑血管病', diseasesList) > 0 " +
            "OR FIND_IN_SET('心脏疾病', diseasesList) > 0 " +
            "OR FIND_IN_SET('肾脏疾病', diseasesList) > 0 " +
            "OR FIND_IN_SET('周围血管病', diseasesList) > 0 " +
            "OR FIND_IN_SET('视网膜病变', diseasesList) > 0 " +
            "OR FIND_IN_SET('糖尿病', diseasesList) > 0 " +
            "OR diseasesList LIKE '%其他%')")
    int countPatientsWithDiseases(@Param("doctorUid") Long doctorUid);


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