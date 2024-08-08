package com.pig4cloud.pig.patient.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;
import com.pig4cloud.pig.patient.entity.PatientNowDiseaseEntity;
import com.pig4cloud.pig.patient.mapper.PatientNowDiseaseMapper;
import com.pig4cloud.pig.patient.service.PatientBaseService;
import com.pig4cloud.pig.patient.service.PatientNowDiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
/**
 * 患者当前疾病信息表
 *
 * @author wangwenche
 * @date 2024-07-03 23:06:46
 */
@Service
public class PatientNowDiseaseServiceImpl extends ServiceImpl<PatientNowDiseaseMapper, PatientNowDiseaseEntity> implements PatientNowDiseaseService {

    @Autowired
    private PatientNowDiseaseMapper patientNowDiseaseMapper;
    // 如果要引用其他表进行多表查询的话用以下的语句
    /*@Autowired
    private PatientBaseService patientBaseService;*/

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

    @Override
    public JSONArray getPatientDiseaseAndTime(Long patientUid) {
        QueryWrapper<PatientNowDiseaseEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_uid", patientUid);
        List<PatientNowDiseaseEntity> diseases = patientNowDiseaseMapper.selectList(queryWrapper);

        JSONArray diseaseArray = new JSONArray();
        for(PatientNowDiseaseEntity disease : diseases) {
            String disease_name = disease.getDisease();
            LocalDate disease_time = disease.getDiseaseTime();
            Period period = Period.between(disease_time, LocalDate.now());
            int years = period.getYears();
            int months = period.getMonths();
            JSONObject diseaseObject = new JSONObject();
            diseaseObject.put("disease_name: ", disease_name);
            diseaseObject.put("year: ", years);
            diseaseObject.put("month: ", months);
            diseaseArray.add(diseaseObject);
        }

        return diseaseArray;
    }

}