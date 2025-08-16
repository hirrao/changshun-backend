package com.pig4cloud.pig.patient.request;

import lombok.Data;

@Data
public class PatientDeviceUpdateRequest {
    Long uid;
    Integer weight;
    Integer height;
}
