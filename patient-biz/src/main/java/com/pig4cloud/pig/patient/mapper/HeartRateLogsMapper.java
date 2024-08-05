package com.pig4cloud.pig.patient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.patient.entity.HeartRateLogsEntity;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HeartRateLogsMapper extends BaseMapper<HeartRateLogsEntity> {

    @Select("SELECT heart_rate, upload_time " +
            "FROM heart_rate_logs " +
            "WHERE patient_uid = #{patientUid} " +
            "ORDER BY upload_time DESC " +
            "LIMIT 1")
    HeartRateLogsEntity getLatestMeasurement(@Param("patientUid") Long patientUid);

}