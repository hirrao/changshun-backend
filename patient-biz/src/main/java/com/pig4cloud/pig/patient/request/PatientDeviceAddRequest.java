package com.pig4cloud.pig.patient.request;

import lombok.Data;

@Data
public class PatientDeviceAddRequest {
    private long uid;
    private int weight;
    private int height;
}
