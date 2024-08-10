package com.pig4cloud.pig.patient.request;

import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PressureHeartRateRequest extends PersureHeartRateEntity {
    private int heartRate;
}
