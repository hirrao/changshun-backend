package com.pig4cloud.pig.patient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.GptMessageEntity;

import java.util.List;

public interface GptMessageService extends IService<GptMessageEntity> {
    List<GptMessageEntity> getMessagesByPatientUid(Long patientUid);
    boolean deleteMessageById(Long messageId);
    boolean deleteAllMessagesByPatientUid(Long patientUid);
}