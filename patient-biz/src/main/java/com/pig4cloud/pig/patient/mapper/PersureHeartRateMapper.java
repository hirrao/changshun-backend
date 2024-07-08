package com.pig4cloud.pig.patient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PersureHeartRateMapper extends BaseMapper<PersureHeartRateEntity> {
    PersureHeartRateEntity selectTodayMaxBloodPressure(@Param("patientUid") Long patientUid);

    PersureHeartRateEntity selectTodayMinHeartRate(@Param("patientUid") Long patientUid);


}