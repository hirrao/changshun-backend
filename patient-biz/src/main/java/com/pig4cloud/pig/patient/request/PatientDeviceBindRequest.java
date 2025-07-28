package com.pig4cloud.pig.patient.request;

import lombok.Data;

@Data
public class PatientDeviceBindRequest {
    private String imei;
    private Long uid;
}
