package com.pig4cloud.pig.patient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PatientBaseMapper extends BaseMapper<PatientBaseEntity> {
    // 分页查询并按care字段降序排序
    @Select("SELECT pb.* FROM patient_base pb INNER JOIN patient_doctor pd ON pb.patient_uid = pd.patient_uid ORDER BY pd.care DESC")
    IPage<PatientBaseEntity> selectPatientBasePageByCare(Page<?> page);

}


