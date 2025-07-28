package com.pig4cloud.pig.patient.request.PatientDeviceCallbackRequest;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author wangyifei
 * 血压测量回调数据
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BloodPressureCallback implements IPatientDeviceCallback {
    private int sbp;
    private int dbp;
    private int hr;
    private boolean goodPosture;
}
