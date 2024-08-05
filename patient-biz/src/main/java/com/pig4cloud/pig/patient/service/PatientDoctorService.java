package com.pig4cloud.pig.patient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.dto.HeartRateStatsDTO;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;

import java.util.List;

public interface PatientDoctorService extends IService<PatientDoctorEntity> {

	List<Long> getPatientUidsByDoctorUid(Long doctorUid);
	
	HeartRateStatsDTO getHeartRateStats(Long doctorUid);
	
	//boolean setPatientDoctorBinding(Long patientUid, Long doctorUid);
	
	R addItem(PatientDoctorEntity patientDoctor);
	
	R updateItem(PatientDoctorEntity patientDoctor);
	long countPatientsByDoctorId(Long doctorUid);

	long countPatientsByDoctorIdAndCare(Long doctorUid);

	long countBloodPressureRecordsByDoctorId(Long doctorUid);

	long countYesterdayAbnormalBloodPressureRecordsByDoctorId(Long doctorUid);
}