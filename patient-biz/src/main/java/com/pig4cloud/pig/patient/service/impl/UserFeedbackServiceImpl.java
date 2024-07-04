package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.UserFeedbackEntity;
import com.pig4cloud.pig.patient.mapper.UserFeedbackMapper;
import com.pig4cloud.pig.patient.service.UserFeedbackService;
import org.springframework.stereotype.Service;
/**
 * 用户反馈信息管理
 *
 * @author huangjiayu
 * @date 2024-07-04 15:59:24
 */
@Service
public class UserFeedbackServiceImpl extends ServiceImpl<UserFeedbackMapper, UserFeedbackEntity> implements UserFeedbackService {
}