package com.pig4cloud.pig.patient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@Mapper
public interface PatientBaseMapper extends BaseMapper<PatientBaseEntity> {
    IPage<PatientBaseEntity> selectPatientPageWithCare(Page<PatientBaseEntity> page);



}