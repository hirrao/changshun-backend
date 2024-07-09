package com.pig4cloud.pig.patient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.dto.HeartRateStatsDTO;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;

public interface PatientDoctorService extends IService<PatientDoctorEntity> {
    HeartRateStatsDTO getHeartRateStats(Long doctorUid);

    //boolean setPatientDoctorBinding(Long patientUid, Long doctorUid);
}