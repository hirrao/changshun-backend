package com.pig4cloud.pig.patient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.patient.entity.SysMessageEntity;

import java.util.List;

public interface SysMessageService extends IService<SysMessageEntity> {
    boolean saveBatch(List<SysMessageEntity> entityList);

    boolean sendMessage(Long doctorUid, Long patientUid, String message);

    void sendMessage(Long doctorUid, Long patientUid, String messageType, String jsonText);
    List<SysMessageEntity> getUnreadMessages(Long patientUid);
    void markMessageAsRead(Long notificationId);
}