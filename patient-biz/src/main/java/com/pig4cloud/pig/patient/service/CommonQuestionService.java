package com.pig4cloud.pig.patient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.CommonQuestionEntity;

import java.util.List;

public interface CommonQuestionService extends IService<CommonQuestionEntity> {
    List<CommonQuestionEntity> getRandomQuestions();
}