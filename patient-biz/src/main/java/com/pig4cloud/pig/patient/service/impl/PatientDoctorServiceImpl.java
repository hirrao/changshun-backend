package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;
import com.pig4cloud.pig.patient.mapper.PatientDoctorMapper;
import com.pig4cloud.pig.patient.service.PatientDoctorService;
import org.springframework.stereotype.Service;
/**
 * 医患绑定表
 *
 * @author 袁钰涛
 * @date 2024-07-05 10:56:25
 */
@Service
public class PatientDoctorServiceImpl extends ServiceImpl<PatientDoctorMapper, PatientDoctorEntity> implements PatientDoctorService {
}