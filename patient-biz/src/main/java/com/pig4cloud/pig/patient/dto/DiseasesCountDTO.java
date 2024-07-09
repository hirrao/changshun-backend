package com.pig4cloud.pig.patient.dto;

import lombok.Data;

@Data
public class DiseasesCountDTO {

    private Long bloodFatAbnormalCount;
    private Long cerebrovascularDiseaseCount;
    private Long heartDiseaseCount;
    private Long kidneyDiseaseCount;
    private Long peripheralVascularDiseaseCount;
    private Long retinopathyCount;
    private Long diabetesCount;
    private Long otherCount;

}
