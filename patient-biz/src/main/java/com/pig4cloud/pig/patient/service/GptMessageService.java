package com.pig4cloud.pig.patient.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.GptMessageEntity;

import java.util.List;

public interface GptMessageService extends IService<GptMessageEntity> {
    List<GptMessageEntity> getMessagesByPatientUid(Long patientUid);
    boolean deleteMessageById(Long messageId);
    boolean deleteAllMessagesByPatientUid(Long patientUid);
    void saveMessage(Long patientUid, String senderType, String content);
    JSONObject handleCommonQuestionClick(long qaId, long patientUid);
}