package com.pig4cloud.pig.patient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface PatientBaseMapper extends BaseMapper<PatientBaseEntity> {
    // 分页查询并按care字段降序排序
    @Select("SELECT pb.* FROM patient_base pb INNER JOIN patient_doctor pd ON pb.patient_uid = pd.patient_uid ORDER BY pd.care DESC")
    IPage<PatientBaseEntity> selectPatientBasePageByCare(Page<?> page);


    @Select("SELECT COUNT(*) FROM patient_base pb " +
            "INNER JOIN patient_doctor pd ON pb.patient_uid = pd.patient_uid " +
            "WHERE pd.doctor_uid = #{doctorUid} " +
            "AND pb.sex = '男' AND YEAR(NOW()) - YEAR(pb.birthday) > 55")
    int countMalePatientsOver55(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM patient_base pb " +
            "INNER JOIN patient_doctor pd ON pb.patient_uid = pd.patient_uid " +
            "WHERE pd.doctor_uid = #{doctorUid} " +
            "AND pb.sex = '男' AND YEAR(NOW()) - YEAR(pb.birthday) <= 55")
    int countMalePatientsUnderEqual55(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM patient_base pb " +
            "INNER JOIN patient_doctor pd ON pb.patient_uid = pd.patient_uid " +
            "WHERE pd.doctor_uid = #{doctorUid} " +
            "AND pb.sex = '女' AND YEAR(NOW()) - YEAR(pb.birthday) > 65")
    int countFemalePatientsOver65(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM patient_base pb " +
            "INNER JOIN patient_doctor pd ON pb.patient_uid = pd.patient_uid " +
            "WHERE pd.doctor_uid = #{doctorUid} " +
            "AND pb.sex = '女' AND YEAR(NOW()) - YEAR(pb.birthday) <= 66")
    int countFemalePatientsUnderEqual66(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM patient_base pb " +
            "INNER JOIN patient_doctor pd ON pb.patient_uid = pd.patient_uid " +
            "WHERE pd.doctor_uid = #{doctorUid} " +
            "AND pd.care = 1 " +
            "AND pb.sex = '男' AND YEAR(NOW()) - YEAR(pb.birthday) > 55")
    int ccountMalePatientsOver55(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM patient_base pb " +
            "INNER JOIN patient_doctor pd ON pb.patient_uid = pd.patient_uid " +
            "WHERE pd.doctor_uid = #{doctorUid} " +
            "AND pd.care = 1 " +
            "AND pb.sex = '男' AND YEAR(NOW()) - YEAR(pb.birthday) <= 55")
    int ccountMalePatientsUnderEqual55(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM patient_base pb " +
            "INNER JOIN patient_doctor pd ON pb.patient_uid = pd.patient_uid " +
            "WHERE pd.doctor_uid = #{doctorUid} " +
            "AND pd.care = 1 " +
            "AND pb.sex = '女' AND YEAR(NOW()) - YEAR(pb.birthday) > 65")
    int ccountFemalePatientsOver65(@Param("doctorUid") Long doctorUid);

    @Select("SELECT COUNT(*) FROM patient_base pb " +
            "INNER JOIN patient_doctor pd ON pb.patient_uid = pd.patient_uid " +
            "WHERE pd.doctor_uid = #{doctorUid} " +
            "AND pd.care = 1 " +
            "AND pb.sex = '女' AND YEAR(NOW()) - YEAR(pb.birthday) <= 66")
    int ccountFemalePatientsUnderEqual66(@Param("doctorUid") Long doctorUid);
}





