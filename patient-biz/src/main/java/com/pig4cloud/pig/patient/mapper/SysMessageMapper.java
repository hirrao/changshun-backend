package com.pig4cloud.pig.patient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.patient.entity.SysMessageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Mapper
public interface SysMessageMapper extends BaseMapper<SysMessageEntity> {



    @Select("SELECT * FROM sys_message WHERE doctor_uid = #{doctorUid} AND message_type = '医生提醒'" +
            "AND sent_date >= #{startDate}")
    List<SysMessageEntity> findRecentMessageByDoctorId(@Param("doctorUid")Long doctorUid, @Param("startDate") LocalDateTime startDate);

}