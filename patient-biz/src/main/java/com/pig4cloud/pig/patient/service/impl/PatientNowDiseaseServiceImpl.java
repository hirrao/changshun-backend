package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.PatientNowDiseaseEntity;
import com.pig4cloud.pig.patient.mapper.PatientNowDiseaseMapper;
import com.pig4cloud.pig.patient.service.PatientNowDiseaseService;
import org.springframework.stereotype.Service;
/**
 * 患者当前疾病信息表
 *
 * @author wangwenche
 * @date 2024-07-03 23:06:46
 */
@Service
public class PatientNowDiseaseServiceImpl extends ServiceImpl<PatientNowDiseaseMapper, PatientNowDiseaseEntity> implements PatientNowDiseaseService {

    /* 示例
    @Override
    public String test(String s) {
        this.get
        return s;
    } */
    /* List<PatientNowDiseaseEntity> getDiseaseByPatientUid(Long patientUid){
        LambdaQueryWrapper<PatientNowDisease> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PatientNowDisease::getPatientUid, patientUid)
                .select(PatientNowDisease::getDisease, PatientNowDisease::getDiseaseTime);
        return repository.selectList(queryWrapper);
    }*/
}