package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.DrugEatTimeEntity;
import com.pig4cloud.pig.patient.mapper.DrugEatTimeMapper;
import com.pig4cloud.pig.patient.service.DrugEatTimeService;
import org.springframework.stereotype.Service;
/**
 * 用药时间对照表（因为一款药可能需要多个时间）
 *
 * @author 袁钰涛
 * @date 2024-07-05 09:57:07
 */
@Service
public class DrugEatTimeServiceImpl extends ServiceImpl<DrugEatTimeMapper, DrugEatTimeEntity> implements DrugEatTimeService {
}