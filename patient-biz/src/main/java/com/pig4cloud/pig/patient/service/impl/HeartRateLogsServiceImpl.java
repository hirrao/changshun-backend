package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.HeartRateLogsEntity;
import com.pig4cloud.pig.patient.mapper.HeartRateLogsMapper;
import com.pig4cloud.pig.patient.service.HeartRateLogsService;
import org.springframework.stereotype.Service;
/**
 * 患者心率数据
 *
 * @author wangwenche
 * @date 2024-08-04 11:45:38
 */
@Service
public class HeartRateLogsServiceImpl extends ServiceImpl<HeartRateLogsMapper, HeartRateLogsEntity> implements HeartRateLogsService {
}