package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;
import com.pig4cloud.pig.patient.mapper.PatientBaseMapper;
import com.pig4cloud.pig.patient.mapper.PatientDoctorMapper;
import com.pig4cloud.pig.patient.service.PatientBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 患者基本信息
 *
 * @author huangjiayu
 * @date 2024-07-03 16:07:48
 */
@Service
public class PatientBaseServiceImpl extends ServiceImpl<PatientBaseMapper, PatientBaseEntity> implements PatientBaseService {
    private final PatientBaseMapper patientBaseMapper;

    public PatientBaseServiceImpl(PatientBaseMapper patientBaseMapper) {
        this.patientBaseMapper = patientBaseMapper;
    }

    @Override
    public IPage<PatientBaseEntity> pageByCare(Page<?> page) {
        return patientBaseMapper.selectPatientBasePageByCare(page);
    }

    @Autowired
    private PatientDoctorMapper patientDoctorMapper;

    @Override
    public int countMalePatientsOver55(Long doctorUid) {
        return patientBaseMapper.countMalePatientsOver55(doctorUid);
    }

    @Override
    public int countMalePatientsUnderEqual55(Long doctorUid) {
        return patientBaseMapper.countMalePatientsUnderEqual55(doctorUid);
    }

    @Override
    public int countFemalePatientsOver65(Long doctorUid) {
        return patientBaseMapper.countFemalePatientsOver65(doctorUid);
    }

    @Override
    public int countFemalePatientsUnderEqual66(Long doctorUid) {
        return patientBaseMapper.countFemalePatientsUnderEqual66(doctorUid);
    }


}