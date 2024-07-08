package com.pig4cloud.pig.patient.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface PatientBaseService extends IService<PatientBaseEntity> {
    // 分页查询并按care字段降序排序
    IPage<PatientBaseEntity> pageByCare(Page<?> page);

}