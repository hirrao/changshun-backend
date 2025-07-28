package com.pig4cloud.pig.patient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.entity.PatientDeviceEntity;
import com.pig4cloud.pig.patient.request.PatientDeviceCallbackRequest.IPatientDeviceCallback;
import org.springframework.stereotype.Service;

@Service
public interface PatientDeviceV2Service extends IService<PatientDeviceEntity> {
    R getByUid(long uid);

    R addPatientDevice(long uid, int weight, int height);

    R bindPatientDevice(String imei, long uid);

    R generateUserAuthToken(long uid);

    /**
     * 手表消息回调处理
     *
     * @return 心永方会忽略所有返回信息, 只对状态码敏感, 所以说不返回错误信息
     */
    R callback(String eventType, Long pddId, Integer timestamp,
               IPatientDeviceCallback eventData);
}
