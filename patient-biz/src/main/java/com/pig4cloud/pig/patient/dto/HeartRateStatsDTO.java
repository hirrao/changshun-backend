package com.pig4cloud.pig.patient.dto;

import lombok.Data;

@Data
public class HeartRateStatsDTO {

    private Long lowHeartRateCount;
    private Long normalHeartRateCount;
    private Long highHeartRateCount;

}
