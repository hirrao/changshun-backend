package com.pig4cloud.pig.patient.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.CommonQuestionEntity;
import com.pig4cloud.pig.patient.entity.GptMessageEntity;
import com.pig4cloud.pig.patient.mapper.CommonQuestionMapper;
import com.pig4cloud.pig.patient.mapper.GptMessageMapper;
import com.pig4cloud.pig.patient.service.GptMessageService;
import jakarta.validation.OverridesAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Autowired
    private CommonQuestionMapper commonQuestionMapper;


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

    @Override
    public void saveMessage(Long patientUid, String senderType, String content) {
        GptMessageEntity message = new GptMessageEntity();
        message.setPatientUid(patientUid);
        message.setSenderType(senderType);
        message.setContent(content);
        message.setSentTime(LocalDateTime.now());
        gptMessageMapper.insert(message);
    }

    @Override
    public JSONObject handleCommonQuestionClick(long qaId, long patientUid) {
        // 查询常见问题和答案
        CommonQuestionEntity questionEntity = commonQuestionMapper.selectById(qaId);
        if (questionEntity == null) {
            throw new RuntimeException("Question not found");
        }

        // 获取问题和答案
        String question = questionEntity.getQuestionText();
        String answer = questionEntity.getAnswerText();

        // 创建问答记录并保存到数据库
        saveMessage(patientUid, "USER", question);
        saveMessage(patientUid, "GPT", answer);

        // 构建返回的JSON对象
        JSONObject result = new JSONObject();
        result.put("question", question);
        result.put("answer", answer);

        return result;
    }
}