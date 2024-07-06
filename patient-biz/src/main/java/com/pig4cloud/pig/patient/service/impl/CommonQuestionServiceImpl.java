package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.CommonQuestionEntity;
import com.pig4cloud.pig.patient.mapper.CommonQuestionMapper;
import com.pig4cloud.pig.patient.service.CommonQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 
 *
 * @author wangwenche
 * @date 2024-07-06 20:21:53
 */
@Service
public class CommonQuestionServiceImpl extends ServiceImpl<CommonQuestionMapper, CommonQuestionEntity> implements CommonQuestionService {
    @Autowired
    private CommonQuestionMapper commonQuestionMapper;

    public List<CommonQuestionEntity> getRandomQuestions(){
        QueryWrapper<CommonQuestionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("RAND()").last("LIMIT 3");
        return commonQuestionMapper.selectList(queryWrapper);
    }
}