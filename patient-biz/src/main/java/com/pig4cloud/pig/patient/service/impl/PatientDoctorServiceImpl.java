package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.dto.HeartRateStatsDTO;
import com.pig4cloud.pig.patient.entity.PatientBmiManaEntity;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;
import com.pig4cloud.pig.patient.mapper.PatientDoctorMapper;
import com.pig4cloud.pig.patient.service.PatientDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 医患绑定表
 *
 * @author wangwenche
 * @date 2024-07-08 23:56:30
 */
@Service
public class PatientDoctorServiceImpl extends ServiceImpl<PatientDoctorMapper, PatientDoctorEntity> implements PatientDoctorService {
    private final PatientDoctorMapper patientDoctorMapper;

    @Autowired
    public PatientDoctorServiceImpl(PatientDoctorMapper patientDoctorMapper) {
        this.patientDoctorMapper = patientDoctorMapper;
    }

    @Override
    public HeartRateStatsDTO getHeartRateStats(Long doctorUid) {
        return patientDoctorMapper.getHeartRateStats(doctorUid);
    }
}