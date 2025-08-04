package com.pig4cloud.pig.patient.request.PatientDeviceCallbackRequest;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author wangyifei
 * 接口包含回调消息中的event_data字段
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "event_type")
@JsonSubTypes({@JsonSubTypes.Type(value = BloodPressureCallback.class, name = "bp_event"), @JsonSubTypes.Type(value = HeartRateCallback.class, name = "hr_event"),})
public interface IPatientDeviceCallback {
}
