package com.pig4cloud.pig.patient.request.PatientDeviceCallbackRequest;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PatientDeviceCallbackRequest {
    private String eventType;
    private Long userId;
    private Integer timestamp;
    private IPatientDeviceCallback eventData;
}

