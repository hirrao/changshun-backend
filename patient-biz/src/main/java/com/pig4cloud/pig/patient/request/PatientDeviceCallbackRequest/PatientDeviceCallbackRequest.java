package com.pig4cloud.pig.patient.request.PatientDeviceCallbackRequest;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PatientDeviceCallbackRequest {
    private String eventType;
    private Long userId;
    private Integer timestamp;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "event_type", defaultImpl = UnknownEventCallback.class)
    @JsonSubTypes({@JsonSubTypes.Type(value = BloodPressureCallback.class, name = "bp_event"), @JsonSubTypes.Type(value = HeartRateCallback.class, name = "hr_event"),})
    private IPatientDeviceCallback eventData;
}

