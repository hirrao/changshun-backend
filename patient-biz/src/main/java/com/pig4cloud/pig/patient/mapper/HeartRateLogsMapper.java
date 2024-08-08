package com.pig4cloud.pig.patient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.patient.entity.HeartRateLogsEntity;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HeartRateLogsMapper extends BaseMapper<HeartRateLogsEntity> {

    @Select("SELECT heart_rate, upload_time " +
            "FROM heart_rate_logs " +
            "WHERE patient_uid = #{patientUid} " +
            "ORDER BY upload_time DESC " +
            "LIMIT 1")
    HeartRateLogsEntity getLatestMeasurement(@Param("patientUid") Long patientUid);

    @Select("SELECT COUNT(*) FROM heart_rate_logs " +
            "WHERE patient_uid IN (SELECT patient_uid FROM patient_doctor WHERE doctor_uid = #{doctorUid}) "
            +
            "AND DATE(upload_time) = CURDATE() " +
            "AND heart_rate < 60")
    int countPatientsWithLowHeartRate(@Param("doctorUid") Long doctorUid);

	@Select("SELECT COUNT(*) FROM heart_rate_logs " +
	 "WHERE patient_uid IN (SELECT patient_uid FROM patient_doctor WHERE doctor_uid = #{doctorUid}) "
	 +
	 "AND DATE(upload_time) = CURDATE() " +
	 "AND heart_rate >= 60 AND heart_rate <= 100")
	int countPatientsWithNormalHeartRate(@Param("doctorUid") Long doctorUid);

	@Select("SELECT COUNT(*) FROM heart_rate_logs " +
	 "WHERE patient_uid IN (SELECT patient_uid FROM patient_doctor WHERE doctor_uid = #{doctorUid}) "
	 +
	 "AND DATE(upload_time) = CURDATE() " +
	 "AND heart_rate > 100")
	int countPatientsWithHighHeartRate(@Param("doctorUid") Long doctorUid);

	@Select("SELECT COUNT(*) FROM heart_rate_logs phr " +
			"INNER JOIN patient_doctor pd ON phr.patient_uid = pd.patient_uid " +
			"WHERE pd.doctor_uid = #{doctorUid} " +
			"AND DATE(phr.upload_time) = CURDATE() " +
			"AND phr.heart_rate < 60 " +
			"AND pd.care = 1")
	int ccountPatientsWithLowHeartRate(@Param("doctorUid") Long doctorUid);

	@Select("SELECT COUNT(*) FROM heart_rate_logs phr " +
			"INNER JOIN patient_doctor pd ON phr.patient_uid = pd.patient_uid " +
			"WHERE pd.doctor_uid = #{doctorUid} " +
			"AND DATE(phr.upload_time) = CURDATE() " +
			"AND phr.heart_rate >= 60 AND phr.heart_rate <= 100 " +
			"AND pd.care = 1")
	int ccountPatientsWithNormalHeartRate(@Param("doctorUid") Long doctorUid);

	@Select("SELECT COUNT(*) FROM heart_rate_logs phr " +
			"INNER JOIN patient_doctor pd ON phr.patient_uid = pd.patient_uid " +
			"WHERE pd.doctor_uid = #{doctorUid} " +
			"AND DATE(phr.upload_time) = CURDATE() " +
			"AND phr.heart_rate > 100 " +
			"AND pd.care = 1")
	int ccountPatientsWithHighHeartRate(@Param("doctorUid") Long doctorUid);

}