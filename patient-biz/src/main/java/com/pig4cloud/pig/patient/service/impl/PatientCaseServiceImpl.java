package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.PatientCaseEntity;
import com.pig4cloud.pig.patient.mapper.PatientCaseMapper;
import com.pig4cloud.pig.patient.service.PatientCaseService;
import org.springframework.stereotype.Service;
/**
 * 患者病历
 *
 * @author huangqing
 * @date 2024-07-07 14:53:34
 */
@Service
public class PatientCaseServiceImpl extends ServiceImpl<PatientCaseMapper, PatientCaseEntity> implements PatientCaseService {
}