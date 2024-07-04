package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.PatientBmiManaEntity;
import com.pig4cloud.pig.patient.mapper.PatientBmiManaMapper;
import com.pig4cloud.pig.patient.service.PatientBmiManaService;
import org.springframework.stereotype.Service;
/**
 * 患者身高体重
 *
 * @author huangqing
 * @date 2024-07-05 01:15:51
 */
@Service
public class PatientBmiManaServiceImpl extends ServiceImpl<PatientBmiManaMapper, PatientBmiManaEntity> implements PatientBmiManaService {
}