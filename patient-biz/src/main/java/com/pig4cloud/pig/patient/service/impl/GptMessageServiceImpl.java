package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.GptMessageEntity;
import com.pig4cloud.pig.patient.mapper.GptMessageMapper;
import com.pig4cloud.pig.patient.service.GptMessageService;
import jakarta.validation.OverridesAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * GPT对话表
 *
 * @author wangwenche
 * @date 2024-07-05 16:50:36
 */
@Service
public class GptMessageServiceImpl extends ServiceImpl<GptMessageMapper, GptMessageEntity> implements GptMessageService {
    @Autowired
    private GptMessageMapper gptMessageMapper;

    @Override
    public List<GptMessageEntity> getMessagesByPatientUid(Long patientUid){
        QueryWrapper<GptMessageEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_uid", patientUid);
        return gptMessageMapper.selectList(queryWrapper);
    }

    @Override
    public boolean deleteMessageById(Long messageId){
        return gptMessageMapper.deleteById(messageId) > 0;
    }

    @Override
    public boolean deleteAllMessagesByPatientUid(Long patientUid){
        QueryWrapper<GptMessageEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_uid", patientUid);
        return gptMessageMapper.delete(queryWrapper) > 0;
    }
}