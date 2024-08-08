package com.pig4cloud.pig.patient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.patient.entity.AiPreDiagnosisEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiPreDiagnosisMapper extends BaseMapper<AiPreDiagnosisEntity> {

    AiPreDiagnosisEntity findLatestByPatientUid(Long patientUid);

    // 查询指定患者的最新AI预问诊表单
    @Select({
            "<script>",
            "SELECT * FROM ai_pre_diagnosis WHERE patientUid IN",
            "<foreach item='uid' collection='patientUids' open='(' separator=',' close=')'>",
            "#{uid}",
            "</foreach>",
            "AND aiId IN (",
            "    SELECT MAX(aiId) FROM ai_pre_diagnosis GROUP BY patientUid",
            ")",
            "</script>"
    })
    List<AiPreDiagnosisEntity> selectLatestDiagnosisByPatientUids(@Param("patientUids") List<Long> patientUids);

    /*@Select("SELECT COUNT(*) FROM ai_pre_diagnosis " +
            "WHERE patient_uid IN " +
            "(SELECT patient_uid FROM patient_doctor WHERE doctor_uid = #{doctorUid} ) " +
            "AND (FIND_IN_SET(#{disease}, diseases_list) > 0)")
    int nocountPatientsWithDisease(@Param("doctorUid") Long doctorUid, @Param("disease") String disease);


    @Select("SELECT COUNT(*) FROM ai_pre_diagnosis " +
            "WHERE patient_uid IN " +
            "(SELECT patient_uid FROM patient_doctor WHERE doctor_uid = #{doctorUid} AND care = 1) " +
            "AND (FIND_IN_SET(#{disease}, diseases_list) > 0)")
    int countPatientsWithDisease(@Param("doctorUid") Long doctorUid, @Param("disease") String disease);


    @Select("SELECT COUNT(*) FROM ai_pre_diagnosis " +
            "WHERE early_cvd_family_history = 1 " +
            "AND patient_uid IN (" +
            "   SELECT patient_uid FROM patient_doctor " +
            "   WHERE doctor_uid = #{doctorUid} " +
            ")")
    int countPatientsWithHypertensionFamilyHistory(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM ai_pre_diagnosis " +
            "WHERE (smoking_status = '是' OR smoking_status = '已戒烟') " +
            "AND patient_uid IN (" +
            "   SELECT patient_uid FROM patient_doctor " +
            "   WHERE doctor_uid = #{doctorUid} " +
            ")")
    int countPatientsWithSmokingHistory(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM ai_pre_diagnosis " +
            "WHERE (drinking_status = '是' OR drinking_status = '已戒酒') " +
            "AND patient_uid IN (" +
            "   SELECT patient_uid FROM patient_doctor " +
            "   WHERE doctor_uid = #{doctorUid} " +
            ")")
    int countPatientsWithDrinkingHistory(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM ai_pre_diagnosis " +
            "WHERE infectious_disease_history IS NOT NULL AND infectious_disease_history != '无' " +
            "AND patient_uid IN (" +
            "   SELECT patient_uid FROM patient_doctor " +
            "   WHERE doctor_uid = #{doctorUid} " +
            ")")
    int countPatientsWithInfectiousHistory(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM ai_pre_diagnosis " +
            "WHERE food_allergy_history IS NOT NULL AND food_allergy_history != '无' " +
            "AND patient_uid IN (" +
            "   SELECT patient_uid FROM patient_doctor " +
            "   WHERE doctor_uid = #{doctorUid} " +
            ")")
    int countPatientsWithFoodAllergyHistory(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM ai_pre_diagnosis " +
            "WHERE early_cvd_family_history = 1 " +
            "AND patient_uid IN (" +
            "   SELECT patient_uid FROM patient_doctor " +
            "   WHERE doctor_uid = #{doctorUid} AND care = 1" +
            ")")
    int ccountPatientsWithHypertensionFamilyHistory(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM ai_pre_diagnosis " +
            "WHERE (smoking_status = '是' OR smoking_status = '已戒烟') " +
            "AND patient_uid IN (" +
            "   SELECT patient_uid FROM patient_doctor " +
            "   WHERE doctor_uid = #{doctorUid} AND care = 1" +
            ")")
    int ccountPatientsWithSmokingHistory(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM ai_pre_diagnosis " +
            "WHERE (drinking_status = '是' OR drinking_status = '已戒酒') " +
            "AND patient_uid IN (" +
            "   SELECT patient_uid FROM patient_doctor " +
            "   WHERE doctor_uid = #{doctorUid} AND care = 1" +
            ")")
    int ccountPatientsWithDrinkingHistory(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM ai_pre_diagnosis " +
            "WHERE infectious_disease_history IS NOT NULL AND infectious_disease_history != '无' " +
            "AND patient_uid IN (" +
            "   SELECT patient_uid FROM patient_doctor " +
            "   WHERE doctor_uid = #{doctorUid} AND care = 1" +
            ")")
    int ccountPatientsWithInfectiousHistory(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM ai_pre_diagnosis " +
            "WHERE food_allergy_history IS NOT NULL AND food_allergy_history != '无' " +
            "AND patient_uid IN (" +
            "   SELECT patient_uid FROM patient_doctor " +
            "   WHERE doctor_uid = #{doctorUid} AND care = 1" +
            ")")
    int ccountPatientsWithFoodAllergyHistory(@Param("doctorUid") Long doctorUid);*/


}