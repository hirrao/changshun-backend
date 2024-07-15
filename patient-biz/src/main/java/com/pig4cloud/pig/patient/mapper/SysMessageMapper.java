package com.pig4cloud.pig.patient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.patient.entity.SysMessageEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMessageMapper extends BaseMapper<SysMessageEntity> {

    List<SysMessageEntity> selectUnreadMessages(Long patientUid);


}