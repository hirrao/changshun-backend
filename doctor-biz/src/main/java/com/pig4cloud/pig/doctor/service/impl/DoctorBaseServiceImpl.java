package com.pig4cloud.pig.doctor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.doctor.entity.DoctorBaseEntity;
import com.pig4cloud.pig.doctor.mapper.DoctorBaseMapper;
import com.pig4cloud.pig.doctor.service.DoctorBaseService;
import org.springframework.stereotype.Service;
/**
 * 医生基础信息管理
 *
 * @author huangjiayu
 * @date 2024-07-03 17:17:03
 */
@Service
public class DoctorBaseServiceImpl extends ServiceImpl<DoctorBaseMapper, DoctorBaseEntity> implements DoctorBaseService {
}