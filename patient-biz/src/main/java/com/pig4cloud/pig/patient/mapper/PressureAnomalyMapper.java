package com.pig4cloud.pig.patient.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig4cloud.pig.patient.entity.PressureAnomalyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface PressureAnomalyMapper extends BaseMapper<PressureAnomalyEntity> {
}