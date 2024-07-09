package com.pig4cloud.pig.patient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pig.patient.dto.HeartRateStatsDTO;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PatientDoctorMapper extends BaseMapper<PatientDoctorEntity> {

    @Select("SELECT " +
            "SUM(CASE WHEN phr.heart_rate < 60 THEN 1 ELSE 0 END) AS lowHeartRateCount, " +
            "SUM(CASE WHEN phr.heart_rate >= 60 AND phr.heart_rate <= 100 THEN 1 ELSE 0 END) AS normalHeartRateCount, " +
            "SUM(CASE WHEN phr.heart_rate > 100 THEN 1 ELSE 0 END) AS highHeartRateCount " +
            "FROM patient_doctor pd " +
            "JOIN persure_heart_rate phr ON pd.patient_uid = phr.patient_uid " +
            "WHERE pd.doctor_uid = #{doctorUid} " +
            "AND pd.care = 1")
    HeartRateStatsDTO getHeartRateStats(@Param("doctorUid") Long doctorUid);



}