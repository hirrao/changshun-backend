package com.pig4cloud.pig.patient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PersureHeartRateMapper extends BaseMapper<PersureHeartRateEntity> {
    PersureHeartRateEntity selectTodayMaxBloodPressure(@Param("patientUid") Long patientUid);

    PersureHeartRateEntity selectTodayMinHeartRate(@Param("patientUid") Long patientUid);

    @Select("SELECT COUNT(*) FROM persure_heart_rate " +
            "WHERE patient_uid IN (SELECT patient_uid FROM patient_doctor WHERE doctor_uid = #{doctorUid}) " +
            "AND DATE(upload_time) = CURDATE() " +
            "AND heart_rate < 60"  )
    int countPatientsWithLowHeartRate(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM persure_heart_rate " +
            "WHERE patient_uid IN (SELECT patient_uid FROM patient_doctor WHERE doctor_uid = #{doctorUid}) " +
            "AND DATE(upload_time) = CURDATE() " +
            "AND heart_rate >= 60 AND heart_rate <= 100")
    int countPatientsWithNormalHeartRate(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM persure_heart_rate " +
            "WHERE patient_uid IN (SELECT patient_uid FROM patient_doctor WHERE doctor_uid = #{doctorUid}) " +
            "AND DATE(upload_time) = CURDATE() " +
            "AND heart_rate > 100")
    int countPatientsWithHighHeartRate(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM persure_heart_rate " +
            "WHERE patient_uid IN (SELECT patient_uid FROM patient_doctor WHERE doctor_uid = #{doctorUid}) " +
            "AND DATE(upload_time) = CURDATE() " +
            "AND heart_rate < 60"  +
            "AND care = 1" )
    int ccountPatientsWithLowHeartRate(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM persure_heart_rate " +
            "WHERE patient_uid IN (SELECT patient_uid FROM patient_doctor WHERE doctor_uid = #{doctorUid}) " +
            "AND DATE(upload_time) = CURDATE() " +
            "AND heart_rate >= 60 AND heart_rate <= 100"+
            "AND care = 1" )
    int ccountPatientsWithNormalHeartRate(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM persure_heart_rate " +
            "WHERE patient_uid IN (SELECT patient_uid FROM patient_doctor WHERE doctor_uid = #{doctorUid}) " +
            "AND DATE(upload_time) = CURDATE() " +
            "AND heart_rate > 100"+
            "AND care = 1")
    int ccountPatientsWithHighHeartRate(@Param("doctorUid") Long doctorUid);


}