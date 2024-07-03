package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.pig.patient.mapper.PatientBaseMapper;
import com.pig4cloud.pig.patient.service.PatientBaseService;
import org.springframework.stereotype.Service;
/**
 * 患者基本信息
 *
 * @author huangjiayu
 * @date 2024-07-03 16:07:48
 */
@Service
public class PatientBaseServiceImpl extends ServiceImpl<PatientBaseMapper, PatientBaseEntity> implements PatientBaseService {
}