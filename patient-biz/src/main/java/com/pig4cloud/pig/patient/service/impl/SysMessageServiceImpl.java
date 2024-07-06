package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.SysMessageEntity;
import com.pig4cloud.pig.patient.mapper.SysMessageMapper;
import com.pig4cloud.pig.patient.service.SysMessageService;
import org.springframework.stereotype.Service;
/**
 * 系统消息表
 *
 * @author 袁钰涛
 * @date 2024-07-05 23:52:47
 */
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessageEntity> implements SysMessageService {
}