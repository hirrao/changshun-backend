package com.pig4cloud.pig.patient.request.PatientDeviceCallbackRequest;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author wangyifei
 * 心率测量回调数据
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HeartRateCallback implements IPatientDeviceCallback {
    private int value;
    private boolean goodPosture;
}
