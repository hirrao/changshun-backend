package com.pig4cloud.pig.patient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.patient.dto.AbnormalBloodDTO;
import com.pig4cloud.pig.patient.dto.PatientiListDTO;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PersureHeartRateMapper extends BaseMapper<PersureHeartRateEntity> {
	
	PersureHeartRateEntity selectTodayMaxBloodPressure(@Param("patientUid") Long patientUid);
	
	PersureHeartRateEntity selectTodayMinHeartRate(@Param("patientUid") Long patientUid);
	
	@Select("SELECT COUNT(*) FROM persure_heart_rate " +
	 "WHERE patient_uid IN (SELECT patient_uid FROM patient_doctor WHERE doctor_uid = #{doctorUid}) "
	 +
	 "AND DATE(upload_time) = CURDATE() " +
	 "AND heart_rate < 60")
	int countPatientsWithLowHeartRate(@Param("doctorUid") Long doctorUid);
	
	@Select("SELECT COUNT(*) FROM persure_heart_rate " +
	 "WHERE patient_uid IN (SELECT patient_uid FROM patient_doctor WHERE doctor_uid = #{doctorUid}) "
	 +
	 "AND DATE(upload_time) = CURDATE() " +
	 "AND heart_rate >= 60 AND heart_rate <= 100")
	int countPatientsWithNormalHeartRate(@Param("doctorUid") Long doctorUid);
	
	@Select("SELECT COUNT(*) FROM persure_heart_rate " +
	 "WHERE patient_uid IN (SELECT patient_uid FROM patient_doctor WHERE doctor_uid = #{doctorUid}) "
	 +
	 "AND DATE(upload_time) = CURDATE() " +
	 "AND heart_rate > 100")
	int countPatientsWithHighHeartRate(@Param("doctorUid") Long doctorUid);

	@Select("SELECT COUNT(*) FROM persure_heart_rate phr " +
			"INNER JOIN patient_doctor pd ON phr.patient_uid = pd.patient_uid " +
			"WHERE pd.doctor_uid = #{doctorUid} " +
			"AND DATE(phr.upload_time) = CURDATE() " +
			"AND phr.heart_rate < 60 " +
			"AND pd.care = 1")
	int ccountPatientsWithLowHeartRate(@Param("doctorUid") Long doctorUid);

	@Select("SELECT COUNT(*) FROM persure_heart_rate phr " +
			"INNER JOIN patient_doctor pd ON phr.patient_uid = pd.patient_uid " +
			"WHERE pd.doctor_uid = #{doctorUid} " +
			"AND DATE(phr.upload_time) = CURDATE() " +
			"AND phr.heart_rate >= 60 AND phr.heart_rate <= 100 " +
			"AND pd.care = 1")
	int ccountPatientsWithNormalHeartRate(@Param("doctorUid") Long doctorUid);

	@Select("SELECT COUNT(*) FROM persure_heart_rate phr " +
			"INNER JOIN patient_doctor pd ON phr.patient_uid = pd.patient_uid " +
			"WHERE pd.doctor_uid = #{doctorUid} " +
			"AND DATE(phr.upload_time) = CURDATE() " +
			"AND phr.heart_rate > 100 " +
			"AND pd.care = 1")
	int ccountPatientsWithHighHeartRate(@Param("doctorUid") Long doctorUid);
	
	@Update("UPDATE persure_heart_rate phr " +
	 "SET sdh_classification = " +
	 "CASE " +
	 "   WHEN phr.systolic BETWEEN 140 AND 159 OR phr.diastolic BETWEEN 90 AND 99 THEN " +
	 "       CASE " +
	 "           WHEN EXISTS (SELECT 1 FROM ai_pre_diagnosis " +
	 "                        WHERE patient_uid = #{patientUid} " +
	 "                          AND ai_id = (SELECT MAX(ai_id) FROM ai_pre_diagnosis " +
	 "                                      WHERE patient_uid = #{patientUid})) " +
	 "                AND (SELECT is_clinical FROM ai_pre_diagnosis " +
	 "                     WHERE patient_uid = #{patientUid} " +
	 "                       AND ai_id = (SELECT MAX(ai_id) FROM ai_pre_diagnosis " +
	 "                                   WHERE patient_uid = #{patientUid})) != 0 THEN '一级高血压高危' "
	 +
	 "           WHEN EXISTS (SELECT 1 FROM ai_pre_diagnosis " +
	 "                        WHERE patient_uid = #{patientUid} " +
	 "                          AND ai_id = (SELECT MAX(ai_id) FROM ai_pre_diagnosis " +
	 "                                      WHERE patient_uid = #{patientUid})) " +
	 "                AND (SELECT danger_reason FROM ai_pre_diagnosis " +
	 "                     WHERE patient_uid = #{patientUid} " +
	 "                       AND ai_id = (SELECT MAX(ai_id) FROM ai_pre_diagnosis " +
	 "                                   WHERE patient_uid = #{patientUid})) = 0 THEN '一级高血压低危' "
	 +
	 "           WHEN EXISTS (SELECT 1 FROM ai_pre_diagnosis " +
	 "                        WHERE patient_uid = #{patientUid} " +
	 "                          AND ai_id = (SELECT MAX(ai_id) FROM ai_pre_diagnosis " +
	 "                                      WHERE patient_uid = #{patientUid})) " +
	 "                AND (SELECT danger_reason FROM ai_pre_diagnosis " +
	 "                     WHERE patient_uid = #{patientUid} " +
	 "                       AND ai_id = (SELECT MAX(ai_id) FROM ai_pre_diagnosis " +
	 "                                   WHERE patient_uid = #{patientUid})) <= 2 THEN '一级高血压中危' "
	 +
	 "           ELSE '一级高血压高危' " +
	 "       END " +
	 "   WHEN phr.systolic BETWEEN 160 AND 179 OR phr.diastolic BETWEEN 100 AND 109 THEN " +
	 "       CASE " +
	 "           WHEN EXISTS (SELECT 1 FROM ai_pre_diagnosis " +
	 "                        WHERE patient_uid = #{patientUid} " +
	 "                          AND ai_id = (SELECT MAX(ai_id) FROM ai_pre_diagnosis " +
	 "                                      WHERE patient_uid = #{patientUid})) " +
	 "                AND (SELECT is_clinical FROM ai_pre_diagnosis " +
	 "                     WHERE patient_uid = #{patientUid} " +
	 "                       AND ai_id = (SELECT MAX(ai_id) FROM ai_pre_diagnosis " +
	 "                                   WHERE patient_uid = #{patientUid})) != 0 THEN '二级高血压高危' "
	 +
	 "           WHEN EXISTS (SELECT 1 FROM ai_pre_diagnosis " +
	 "                        WHERE patient_uid = #{patientUid} " +
	 "                          AND ai_id = (SELECT MAX(ai_id) FROM ai_pre_diagnosis " +
	 "                                      WHERE patient_uid = #{patientUid})) " +
	 "                AND (SELECT danger_reason FROM ai_pre_diagnosis " +
	 "                     WHERE patient_uid = #{patientUid} " +
	 "                       AND ai_id = (SELECT MAX(ai_id) FROM ai_pre_diagnosis " +
	 "                                   WHERE patient_uid = #{patientUid})) <= 2 THEN '二级高血压中危' "
	 +
	 "           ELSE '二级高血压高危' " +
	 "       END " +
	 "   WHEN phr.systolic >= 180 OR phr.diastolic >= 110 THEN '三级高血压高危' " +
	 "   ELSE '未分类' " +
	 "END " +
	 "WHERE phr.sdh_id = #{sdhId}")
	void updateSdhClassification(@Param("sdhId") Long sdhId, @Param("patientUid") Long patientUid);
	
	@Select("SELECT sdh_classification, COUNT(*) AS count " +
	 "FROM persure_heart_rate phr " +
	 "JOIN patient_doctor pd ON phr.patient_uid = pd.patient_uid " +
	 "WHERE pd.doctor_uid = #{doctorUid} " +
	 "  AND pd.care = 1 " +
	 "  AND DATE(phr.upload_time) = CURDATE() " +
	 "GROUP BY sdh_classification")
	List<Map<String, Object>> countSdhClassificationByDoctorAndCare(
	 @Param("doctorUid") Long doctorUid);
	
	@Select("SELECT sdh_classification, COUNT(*) AS count " +
	 "FROM persure_heart_rate phr " +
	 "JOIN patient_doctor pd ON phr.patient_uid = pd.patient_uid " +
	 "WHERE pd.doctor_uid = #{doctorUid} " +
	 "  AND DATE(phr.upload_time) = CURDATE() " +
	 "GROUP BY sdh_classification")
	List<Map<String, Object>> nocountSdhClassificationByDoctorAndCare(
	 @Param("doctorUid") Long doctorUid);

	@Select("SELECT DATE(p.upload_time) AS date, COUNT(*) AS count " +
			"FROM persure_heart_rate p " +
			"JOIN patient_doctor pd ON p.patient_uid = pd.patient_uid " +
			"WHERE pd.doctor_uid = #{doctorUid} " +
			"AND p.upload_time >= DATE_SUB(CURDATE(), INTERVAL 10 DAY) " +
			"GROUP BY DATE(p.upload_time) " +
			"ORDER BY DATE(p.upload_time)")
	List<Map<String, Object>> selectDailyStatistics(@Param("doctorUid") Long doctorUid);
	
	// 查询所有患者的血压病例数据
	Page<PatientiListDTO> selectPatientList(Page page,
	 @Param("query") PatientiListDTO patientiListDTO);
	
	//	查询异常血压记录
	Page<AbnormalBloodDTO> selectAbnormalBloodList(Page page,
	 @Param("query") AbnormalBloodDTO abnormalBloodDTO);

	void updateRiskAssessment(@Param("sdhId") Long sdhId, @Param("riskAssessment") String riskAssessment);
	
}