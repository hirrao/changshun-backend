package com.pig4cloud.pig.patient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.patient.entity.PatientBmiManaEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface PatientBmiManaMapper extends BaseMapper<PatientBmiManaEntity> {

    @Select("SELECT height, weight, bmimeasurement_date " +
            "FROM patient_bmi_mana " +
            "WHERE patient_uid = #{patientUid} " +
            "ORDER BY bmimeasurement_date DESC " +
            "LIMIT 1 ")
    Map<String, Object> getLatestHeightWeight(@Param("patientUid") Long patientUid);

    @Select("SELECT height, weight, bmimeasurement_date " +
            "FROM patient_bmi_mana " +
            "WHERE patient_uid = #{patientUid} " +
            "ORDER BY bmimeasurement_date DESC " +
            "LIMIT 7 ")
    List<Map<String, Object>> getLastestSevenBmiRecord(@Param("patientUid") Long patientUid);
}