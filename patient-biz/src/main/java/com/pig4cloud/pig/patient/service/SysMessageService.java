package com.pig4cloud.pig.patient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.SysMessageEntity;

import java.util.List;

public interface SysMessageService extends IService<SysMessageEntity> {
    boolean saveBatch(List<SysMessageEntity> entityList);

}