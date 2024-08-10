package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.dto.StatisticsResult;
import com.pig4cloud.pig.patient.entity.AiPreDiagnosisEntity;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;
import com.pig4cloud.pig.patient.mapper.AiPreDiagnosisMapper;
import com.pig4cloud.pig.patient.mapper.PatientDoctorMapper;
import com.pig4cloud.pig.patient.service.AiPreDiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

/**
 * AI预问诊
 *
 * @author wangwenche
 * @date 2024-07-07 11:55:10
 */
@Service
public class AiPreDiagnosisServiceImpl extends ServiceImpl<AiPreDiagnosisMapper, AiPreDiagnosisEntity> implements AiPreDiagnosisService {
    @Autowired
    private PatientDoctorMapper patientDoctorMapper;

    @Autowired
    private AiPreDiagnosisMapper aiPreDiagnosisMapper;


    /*@Autowired
    public AiPreDiagnosisServiceImpl(AiPreDiagnosisMapper aiPreDiagnosisMapper) {
        this.aiPreDiagnosisMapper = aiPreDiagnosisMapper;
    }*/


    @Override
    public R<Map<String, Integer>> getPatientDiseasesCount(Long doctorUid) {
        // 获取与医生绑定且 care 字段为 1 的患者
        QueryWrapper<PatientDoctorEntity> patientDoctorQuery = new QueryWrapper<>();
        patientDoctorQuery.eq("doctor_uid", doctorUid);
        List<PatientDoctorEntity> patientDoctors = patientDoctorMapper.selectList(patientDoctorQuery);

        Set<Long> patientUids = new HashSet<>();
        for (PatientDoctorEntity patientDoctor : patientDoctors) {
            patientUids.add(patientDoctor.getPatientUid());
        }

        // 查询与患者相关的 AI 预问诊记录
        QueryWrapper<AiPreDiagnosisEntity> aiPreDiagnosisQuery = new QueryWrapper<>();
        aiPreDiagnosisQuery.in("patient_uid", patientUids);
        List<AiPreDiagnosisEntity> aiPreDiagnosisList = aiPreDiagnosisMapper.selectList(aiPreDiagnosisQuery);

        // 使用 Set 存储每个患者的最新记录
        Map<Long, AiPreDiagnosisEntity> latestDiagnosisMap = new HashMap<>();

        // 遍历所有 AI 预问诊记录，选择每个患者的最新记录
        for (AiPreDiagnosisEntity diagnosis : aiPreDiagnosisList) {
            Long patientUid = diagnosis.getPatientUid();
            // 如果当前患者没有记录，或当前记录比已存储的记录新，则更新
            if (!latestDiagnosisMap.containsKey(patientUid) ||
                    diagnosis.getAiId() > latestDiagnosisMap.get(patientUid).getAiId()) {
                latestDiagnosisMap.put(patientUid, diagnosis);
            }
        }

        // 初始化疾病统计结果
        Map<String, Integer> diseasesCount = new HashMap<>();
        diseasesCount.put("血脂异常", 0);
        diseasesCount.put("脑血管病", 0);
        diseasesCount.put("心脏疾病", 0);
        diseasesCount.put("肾脏疾病", 0);
        diseasesCount.put("周围血管病", 0);
        diseasesCount.put("视网膜病变", 0);
        diseasesCount.put("糖尿病", 0);
        diseasesCount.put("其他", 0);

        // 统计每个患者最新AI预问诊记录中的疾病
        for (AiPreDiagnosisEntity diagnosis : latestDiagnosisMap.values()) {
            String diseasesList = diagnosis.getDiseasesList(); // 获取疾病列表

            // 在调用 split 前进行空值检查
            if (diseasesList != null && !diseasesList.isEmpty()) {
                String[] diseases = diseasesList.split(",");
                for (String disease : diseases) {
                    disease = disease.trim(); // 去除多余空格
                    if (diseasesCount.containsKey(disease)) {
                        diseasesCount.put(disease, diseasesCount.get(disease) + 1);
                    } else {
                        diseasesCount.put("其他", diseasesCount.get("其他") + 1);
                    }
                }
            }
        }

        return R.ok(diseasesCount);
    }

    @Override
    public R<Map<String, Integer>> getCarePatientDiseasesCount(Long doctorUid) {
        // 获取与医生绑定且 care 字段为 1 的患者
        QueryWrapper<PatientDoctorEntity> patientDoctorQuery = new QueryWrapper<>();
        patientDoctorQuery.eq("doctor_uid", doctorUid).eq("care", 1);
        List<PatientDoctorEntity> patientDoctors = patientDoctorMapper.selectList(patientDoctorQuery);

        Set<Long> patientUids = new HashSet<>();
        for (PatientDoctorEntity patientDoctor : patientDoctors) {
            patientUids.add(patientDoctor.getPatientUid());
        }

        // 查询与患者相关的 AI 预问诊记录
        QueryWrapper<AiPreDiagnosisEntity> aiPreDiagnosisQuery = new QueryWrapper<>();
        aiPreDiagnosisQuery.in("patient_uid", patientUids);
        List<AiPreDiagnosisEntity> aiPreDiagnosisList = aiPreDiagnosisMapper.selectList(aiPreDiagnosisQuery);

        // 使用 Set 存储每个患者的最新记录
        Map<Long, AiPreDiagnosisEntity> latestDiagnosisMap = new HashMap<>();

        // 遍历所有 AI 预问诊记录，选择每个患者的最新记录
        for (AiPreDiagnosisEntity diagnosis : aiPreDiagnosisList) {
            Long patientUid = diagnosis.getPatientUid();
            // 如果当前患者没有记录，或当前记录比已存储的记录新，则更新
            if (!latestDiagnosisMap.containsKey(patientUid) ||
                    diagnosis.getAiId() > latestDiagnosisMap.get(patientUid).getAiId()) {
                latestDiagnosisMap.put(patientUid, diagnosis);
            }
        }

        // 初始化疾病统计结果
        Map<String, Integer> diseasesCount = new HashMap<>();
        diseasesCount.put("血脂异常", 0);
        diseasesCount.put("脑血管病", 0);
        diseasesCount.put("心脏疾病", 0);
        diseasesCount.put("肾脏疾病", 0);
        diseasesCount.put("周围血管病", 0);
        diseasesCount.put("视网膜病变", 0);
        diseasesCount.put("糖尿病", 0);
        diseasesCount.put("其他", 0);

        // 统计每个患者最新AI预问诊记录中的疾病
        for (AiPreDiagnosisEntity diagnosis : latestDiagnosisMap.values()) {
            String diseasesList = diagnosis.getDiseasesList(); // 获取疾病列表

            // 在调用 split 前进行空值检查
            if (diseasesList != null && !diseasesList.isEmpty()) {
                String[] diseases = diseasesList.split(",");
                for (String disease : diseases) {
                    disease = disease.trim(); // 去除多余空格
                    if (diseasesCount.containsKey(disease)) {
                        diseasesCount.put(disease, diseasesCount.get(disease) + 1);
                    } else {
                        diseasesCount.put("其他", diseasesCount.get("其他") + 1);
                    }
                }
            }
        }

        return R.ok(diseasesCount);
    }

    @Override
    public StatisticsResult getStatisticsByDoctor(Long doctorUid) {
        // 1. 获取与医生绑定的患者
        QueryWrapper<PatientDoctorEntity> patientDoctorQuery = new QueryWrapper<>();
        patientDoctorQuery.eq("doctor_uid", doctorUid);
        List<PatientDoctorEntity> patientDoctors = patientDoctorMapper.selectList(patientDoctorQuery);

        Set<Long> patientUids = new HashSet<>();
        for (PatientDoctorEntity patientDoctor : patientDoctors) {
            patientUids.add(patientDoctor.getPatientUid());
        }

        // 总患者人数
        int totalPatients = patientUids.size();
        if (totalPatients == 0) {
            return new StatisticsResult(0, 0, 0, 0, 0, 0, 0, 0, 0, 0); // 如果没有患者，直接返回0
        }

        // 2. 查询所有与患者相关的AI预问诊记录
        QueryWrapper<AiPreDiagnosisEntity> aiPreDiagnosisQuery = new QueryWrapper<>();
        aiPreDiagnosisQuery.in("patient_uid", patientUids);
        List<AiPreDiagnosisEntity> aiPreDiagnosisList = aiPreDiagnosisMapper.selectList(aiPreDiagnosisQuery);

        // 3. 使用 Map 存储每个患者的最新记录
        Map<Long, AiPreDiagnosisEntity> latestDiagnosisMap = new HashMap<>();

        // 遍历所有 AI 预问诊记录，选择每个患者的最新记录
        for (AiPreDiagnosisEntity diagnosis : aiPreDiagnosisList) {
            Long patientUid = diagnosis.getPatientUid();
            // 如果当前患者没有记录，或当前记录比已存储的记录新，则更新
            if (!latestDiagnosisMap.containsKey(patientUid) ||
                    diagnosis.getAiId() > latestDiagnosisMap.get(patientUid).getAiId()) {
                latestDiagnosisMap.put(patientUid, diagnosis);
            }
        }

        // 4. 统计条件
        int earlyCvdFamilyHistoryCount = 0;
        int smokingCount = 0;
        int drinkingCount = 0;
        int infectiousDiseaseCount = 0;
        int foodAllergyCount = 0;

        // 遍历每个患者的最新记录进行统计
        for (AiPreDiagnosisEntity diagnosis : latestDiagnosisMap.values()) {
            // 统计条件
            if (diagnosis.getEarlyCvdFamilyHistory() == 1) {
                earlyCvdFamilyHistoryCount++;
            }
            if ("是".equals(diagnosis.getSmokingStatus()) || "已戒烟".equals(diagnosis.getSmokingStatus())) {
                smokingCount++;
            }
            if ("是".equals(diagnosis.getDrinkingStatus()) || "已戒酒".equals(diagnosis.getDrinkingStatus())) {
                drinkingCount++;
            }
            if (diagnosis.getInfectiousDiseaseHistory() != null && !diagnosis.getInfectiousDiseaseHistory().equals("无")) {
                infectiousDiseaseCount++;
            }
            if (diagnosis.getFoodAllergyHistory() != null && !diagnosis.getFoodAllergyHistory().equals("无")) {
                foodAllergyCount++;
            }
        }

        // 计算百分比
        double earlyCvdFamilyHistoryPercentage = (double) earlyCvdFamilyHistoryCount / totalPatients * 100;
        double smokingPercentage = (double) smokingCount / totalPatients * 100;
        double drinkingPercentage = (double) drinkingCount / totalPatients * 100;
        double infectiousDiseasePercentage = (double) infectiousDiseaseCount / totalPatients * 100;
        double foodAllergyPercentage = (double) foodAllergyCount / totalPatients * 100;

        return new StatisticsResult(
                earlyCvdFamilyHistoryCount,
                smokingCount,
                drinkingCount,
                infectiousDiseaseCount,
                foodAllergyCount, earlyCvdFamilyHistoryPercentage, smokingPercentage, drinkingPercentage, infectiousDiseasePercentage, foodAllergyPercentage
        );
    }
    @Override
    public StatisticsResult getCareStatisticsByDoctor(Long doctorUid) {
        // 1. 获取与医生绑定的患者
        QueryWrapper<PatientDoctorEntity> patientDoctorQuery = new QueryWrapper<>();
        patientDoctorQuery.eq("doctor_uid", doctorUid).eq("care", 1);
        List<PatientDoctorEntity> patientDoctors = patientDoctorMapper.selectList(patientDoctorQuery);

        Set<Long> patientUids = new HashSet<>();
        for (PatientDoctorEntity patientDoctor : patientDoctors) {
            patientUids.add(patientDoctor.getPatientUid());
        }

        // 总患者人数
        int totalPatients = patientUids.size();
        if (totalPatients == 0) {
            return new StatisticsResult(0, 0, 0, 0, 0, 0, 0, 0, 0, 0); // 如果没有患者，直接返回0
        }

        // 2. 查询所有与患者相关的AI预问诊记录
        QueryWrapper<AiPreDiagnosisEntity> aiPreDiagnosisQuery = new QueryWrapper<>();
        aiPreDiagnosisQuery.in("patient_uid", patientUids);
        List<AiPreDiagnosisEntity> aiPreDiagnosisList = aiPreDiagnosisMapper.selectList(aiPreDiagnosisQuery);

        // 3. 使用 Map 存储每个患者的最新记录
        Map<Long, AiPreDiagnosisEntity> latestDiagnosisMap = new HashMap<>();

        // 遍历所有 AI 预问诊记录，选择每个患者的最新记录
        for (AiPreDiagnosisEntity diagnosis : aiPreDiagnosisList) {
            Long patientUid = diagnosis.getPatientUid();
            // 如果当前患者没有记录，或当前记录比已存储的记录新，则更新
            if (!latestDiagnosisMap.containsKey(patientUid) ||
                    diagnosis.getAiId() > latestDiagnosisMap.get(patientUid).getAiId()) {
                latestDiagnosisMap.put(patientUid, diagnosis);
            }
        }

        // 4. 统计条件
        int earlyCvdFamilyHistoryCount = 0;
        int smokingCount = 0;
        int drinkingCount = 0;
        int infectiousDiseaseCount = 0;
        int foodAllergyCount = 0;

        // 遍历每个患者的最新记录进行统计
        for (AiPreDiagnosisEntity diagnosis : latestDiagnosisMap.values()) {
            // 统计条件
            if (diagnosis.getEarlyCvdFamilyHistory() == 1) {
                earlyCvdFamilyHistoryCount++;
            }
            if ("是".equals(diagnosis.getSmokingStatus()) || "已戒烟".equals(diagnosis.getSmokingStatus())) {
                smokingCount++;
            }
            if ("是".equals(diagnosis.getDrinkingStatus()) || "已戒酒".equals(diagnosis.getDrinkingStatus())) {
                drinkingCount++;
            }
            if (diagnosis.getInfectiousDiseaseHistory() != null && !diagnosis.getInfectiousDiseaseHistory().equals("无")) {
                infectiousDiseaseCount++;
            }
            if (diagnosis.getFoodAllergyHistory() != null && !diagnosis.getFoodAllergyHistory().equals("无")) {
                foodAllergyCount++;
            }
        }

        // 计算百分比
        double earlyCvdFamilyHistoryPercentage = (double) earlyCvdFamilyHistoryCount / totalPatients * 100;
        double smokingPercentage = (double) smokingCount / totalPatients * 100;
        double drinkingPercentage = (double) drinkingCount / totalPatients * 100;
        double infectiousDiseasePercentage = (double) infectiousDiseaseCount / totalPatients * 100;
        double foodAllergyPercentage = (double) foodAllergyCount / totalPatients * 100;

        return new StatisticsResult(
                earlyCvdFamilyHistoryCount,
                smokingCount,
                drinkingCount,
                infectiousDiseaseCount,
                foodAllergyCount, earlyCvdFamilyHistoryPercentage, smokingPercentage, drinkingPercentage, infectiousDiseasePercentage, foodAllergyPercentage
        );
    }


    @Override
    public String generateAiPreDiagnosisReport(Long patientUid) {
        QueryWrapper<AiPreDiagnosisEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("patient_uid", patientUid).orderByDesc("ai_id").last("LIMIT 1");
        AiPreDiagnosisEntity latestDiagnosis = aiPreDiagnosisMapper.selectOne(queryWrapper);

        if (latestDiagnosis == null) {
            return "未找到该患者的AI预问诊表单。";
        }

        // 处理 diseasesList 字段
        String diseasesList = latestDiagnosis.getDiseasesList();
        String diseasesStatement;
        if ("无".equals(diseasesList)) {
            diseasesStatement = "否认血脂异常，脑血管病，心脏疾病，肾脏疾病，周围血管病，视网膜病变，糖尿病等病史";
        } else if ("不清楚".equals(diseasesList)) {
            diseasesStatement = "不确定血脂异常，脑血管病，心脏疾病，肾脏疾病，周围血管病，视网膜病变，糖尿病等病史";
        } else {
            diseasesStatement = "承认" + diseasesList + "病史";
        }

        // 处理 infectiousDiseaseHistory 字段
        String infectiousDiseaseHistory = latestDiagnosis.getInfectiousDiseaseHistory();
        String infectiousDiseaseStatement = "无".equals(infectiousDiseaseHistory) ?
                "否认传染病史" :
                "有" + infectiousDiseaseHistory + "传染病";

        // 处理 foodAllergyHistory 字段
        String foodAllergyHistory = latestDiagnosis.getFoodAllergyHistory();
        String foodAllergyStatement = "无".equals(foodAllergyHistory) ?
                "否认食物过敏史" :
                "对" + foodAllergyHistory + "过敏";

        return diseasesStatement + "。" + infectiousDiseaseStatement + "。" + foodAllergyStatement + "。";
    }
    @Override
    public String getSmokingAndDrinkingInfo(Long patientUid) {
        LambdaQueryWrapper<AiPreDiagnosisEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AiPreDiagnosisEntity::getPatientUid, patientUid)
                .orderByDesc(AiPreDiagnosisEntity::getAiId)
                .last("LIMIT 1");

        AiPreDiagnosisEntity entity = aiPreDiagnosisMapper.selectOne(queryWrapper);

        if (entity == null) {
            return "未找到预问诊信息";
        }

        String smokingInfo = "";
        if ("否".equals(entity.getSmokingStatus())) {
            smokingInfo = "不吸烟";
        } else if ("是".equals(entity.getSmokingStatus())) {
            smokingInfo = "自述吸烟" + entity.getSmokingDuration() + "年，每日吸烟量" + entity.getDailySmokingAmount() + "根";
        } else if ("已戒烟".equals(entity.getSmokingStatus())) {
            smokingInfo = "自述曾吸烟" + entity.getSmokingDuration() + "年，每日吸烟量" + entity.getDailySmokingAmount() + "根";
        }

        String drinkingInfo = "";
        if ("否".equals(entity.getDrinkingStatus())) {
            drinkingInfo = "不饮酒";
        } else if ("是".equals(entity.getDrinkingStatus())) {
            drinkingInfo = "自述饮酒" + entity.getDrinkingDuration() + "年，每日饮酒量" + entity.getDailyDrinkingAmount() + "ml";
        } else if ("已戒酒".equals(entity.getDrinkingStatus())) {
            drinkingInfo = "自述曾饮酒" + entity.getDrinkingDuration() + "年，每日饮酒量" + entity.getDailyDrinkingAmount() + "ml";
        }

        return smokingInfo + "；" + drinkingInfo;
    }

    @Override
    public String getDiagnosisDetails(Long patientUid) {
        QueryWrapper<AiPreDiagnosisEntity> query = Wrappers.query();
        query.eq("patient_uid", patientUid).orderByDesc("ai_id").last("LIMIT 1");
        AiPreDiagnosisEntity diagnosis = this.baseMapper.selectOne(query);
        if (diagnosis == null) {
            return "未找到家族史记录";
        }
        String earlyCvdHistory = diagnosis.getEarlyCvdFamilyHistory() == 1 ? "自述有早发心血管家族史" : "否认早发心血管家族史";
        String geneticDisease = "无".equals(diagnosis.getGeneticDiseaseInFamily()) ? "否认直系亲属中遗传病史" : "自述" + diagnosis.getGeneticDiseaseInFamily();
        return earlyCvdHistory + ". " + geneticDisease + ".";
    }






    /*@Override
    public Map<String, Integer> nocountPatientsWithDiseases(Long doctorUid) {
        Map<String, String> diseaseNameToKeyMap = new HashMap<>();
        diseaseNameToKeyMap.put("糖尿病", "DM");
        diseaseNameToKeyMap.put("视网膜病变", "DR");
        diseaseNameToKeyMap.put("周围血管病", "PAD");
        diseaseNameToKeyMap.put("肾脏疾病", "CKD");
        diseaseNameToKeyMap.put("脑血管病", "CVD");
        diseaseNameToKeyMap.put("血脂异常", "BL");
        diseaseNameToKeyMap.put("其他", "others");

        List<String> diseaseNames = Arrays.asList("糖尿病", "视网膜病变", "周围血管病", "肾脏疾病",
                "脑血管病", "血脂异常", "其他");

        Map<String, Integer> diseaseCounts = new HashMap<>();
        for (String disease : diseaseNames) {
            int count = aiPreDiagnosisMapper.nocountPatientsWithDisease(doctorUid, disease);
            String key = diseaseNameToKeyMap.get(disease);
            diseaseCounts.put(key, count);
        }

        return diseaseCounts;
    }

    @Override
    public Map<String, Integer> countPatientsWithDiseases(Long doctorUid) {
        // 中文疾病名称到英文缩写的映射
        Map<String, String> diseaseNameToKeyMap = new HashMap<>();
        diseaseNameToKeyMap.put("糖尿病", "DM");
        diseaseNameToKeyMap.put("视网膜病变", "DR");
        diseaseNameToKeyMap.put("周围血管病", "PAD");
        diseaseNameToKeyMap.put("肾脏疾病", "CKD");
        diseaseNameToKeyMap.put("脑血管病", "CVD");
        diseaseNameToKeyMap.put("血脂异常", "BL");
        diseaseNameToKeyMap.put("其他", "others");

        List<String> diseaseNames = Arrays.asList("糖尿病", "视网膜病变", "周围血管病", "肾脏疾病",
                "脑血管病", "血脂异常", "其他");

        Map<String, Integer> diseaseCounts = new HashMap<>();
        for (String disease : diseaseNames) {
            int count = aiPreDiagnosisMapper.countPatientsWithDisease(doctorUid, disease);
            String key = diseaseNameToKeyMap.get(disease);
            diseaseCounts.put(key, count);
        }

        return diseaseCounts;
    }


    @Override
    public int countPatientsWithHypertensionFamilyHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.countPatientsWithHypertensionFamilyHistory(doctorUid);
    }

    @Override
    public int countPatientsWithSmokingHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.countPatientsWithSmokingHistory(doctorUid);
    }

    @Override
    public int countPatientsWithDrinkingHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.countPatientsWithDrinkingHistory(doctorUid);
    }

    @Override
    public int countPatientsWithInfectiousHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.countPatientsWithInfectiousHistory(doctorUid);
    }

    @Override
    public int countPatientsWithFoodAllergyHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.countPatientsWithFoodAllergyHistory(doctorUid);
    }


    @Override
    public int ccountPatientsWithHypertensionFamilyHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.ccountPatientsWithHypertensionFamilyHistory(doctorUid);
    }

    @Override
    public int ccountPatientsWithSmokingHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.ccountPatientsWithSmokingHistory(doctorUid);
    }

    @Override
    public int ccountPatientsWithDrinkingHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.ccountPatientsWithDrinkingHistory(doctorUid);
    }

    @Override
    public int ccountPatientsWithInfectiousHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.ccountPatientsWithInfectiousHistory(doctorUid);
    }

    @Override
    public int ccountPatientsWithFoodAllergyHistory(Long doctorUid) {
        return aiPreDiagnosisMapper.ccountPatientsWithFoodAllergyHistory(doctorUid);
    }*/

    @Override
    public boolean saveAiPreDiagnosis(AiPreDiagnosisEntity aiPreDiagnosis) {
        int riskFactorsCount = calculateRiskFactors(aiPreDiagnosis);
        aiPreDiagnosis.setDangerReason(riskFactorsCount);
        return saveOrUpdate(aiPreDiagnosis);
    }

    @Override
    public boolean updateAiPreDiagnosis(AiPreDiagnosisEntity aiPreDiagnosis) {
        int riskFactorsCount = calculateRiskFactors(aiPreDiagnosis);
        aiPreDiagnosis.setDangerReason(riskFactorsCount);
        return updateById(aiPreDiagnosis);
    }

    private int calculateRiskFactors(AiPreDiagnosisEntity aiPreDiagnosis) {
        int riskFactorsCount = 0;

        // 根据你的危险因素定义，实现具体逻辑
        if (isMaleOver55(aiPreDiagnosis) || isFemaleOver65(aiPreDiagnosis)) {
            riskFactorsCount++;
        }
        if ("是".equals(aiPreDiagnosis.getSmokingStatus())) {
            riskFactorsCount++;
        }
        if (aiPreDiagnosis.getEarlyCvdFamilyHistory() == 1) {
            riskFactorsCount++;
        }
        // 判断是否有血脂异常
        if (hasDiseasesList(aiPreDiagnosis.getDiseasesList(), "血脂异常")) {
            riskFactorsCount++;
        }

        return riskFactorsCount;
    }

    private boolean isMaleOver55(AiPreDiagnosisEntity aiPreDiagnosis) {
        if (aiPreDiagnosis.getBirthday() == null) {
            return false;
        }
        return "男性".equals(aiPreDiagnosis.getGender()) && calculateAge(aiPreDiagnosis.getBirthday()) > 55;
    }

    private boolean isFemaleOver65(AiPreDiagnosisEntity aiPreDiagnosis) {
        if (aiPreDiagnosis.getBirthday() == null) {
            return false;
        }
        return "女性".equals(aiPreDiagnosis.getGender()) && calculateAge(aiPreDiagnosis.getBirthday()) > 65;
    }

    private int calculateAge(LocalDate birthday) {
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    /**
     * 判断患病列表中是否包含特定的疾病
     * @param diseasesList 患病列表，逗号分隔的字符串
     * @param diseaseName 疾病名称
     * @return 是否包含
     */
    private boolean hasDiseasesList(String diseasesList, String diseaseName) {
        if (diseasesList == null || diseasesList.isEmpty()) {
            return false;
        }
        // 将患病列表按逗号分隔成数组
        String[] diseases = diseasesList.split(",");
        for (String disease : diseases) {
            // 去除空格后进行比较
            if (disease.trim().equals(diseaseName)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public Boolean calculateIsClinical(AiPreDiagnosisEntity aiPreDiagnosis) {
        if (aiPreDiagnosis.getDiseasesList() != null) {
            String diseasesList = aiPreDiagnosis.getDiseasesList().toLowerCase(); // 转为小写便于比较
            // 判断是否包含以下关键词
            boolean hasClinicalDisease = diseasesList.contains("脑血管病") ||
                    diseasesList.contains("心脏疾病") ||
                    diseasesList.contains("肾脏疾病") ||
                    diseasesList.contains("周围血管病") ||
                    diseasesList.contains("视网膜病变") ||
                    diseasesList.contains("糖尿病");
            return hasClinicalDisease;
        }
        return false;
    }

    @Override
    public boolean saveOrUpdate(AiPreDiagnosisEntity entity) {
        boolean success = super.saveOrUpdate(entity);
        // 更新 isClinical 字段
        entity.setIsClinical(calculateIsClinical(entity));
        super.updateById(entity);
        return success;
    }

    /*@Override
    public JSONObject ccountPatientsHistory(Long doctorUid) {
        Map<String, Object> historyCounts = new LinkedHashMap<>();

        // 获取统计结果
        int hypertensionFamilyHistoryCount = ccountPatientsWithHypertensionFamilyHistory(doctorUid);
        int smokingHistoryCount = ccountPatientsWithSmokingHistory(doctorUid);
        int drinkingHistoryCount = ccountPatientsWithDrinkingHistory(doctorUid);
        int infectiousHistoryCount = ccountPatientsWithInfectiousHistory(doctorUid);
        int foodAllergyHistoryCount = ccountPatientsWithFoodAllergyHistory(doctorUid);
        int total = patientDoctorMapper.getDoctorCountByCare(doctorUid);

        DecimalFormat df = new DecimalFormat("#.##");

        // 放入结果到Map中
        historyCounts.put("高血压家族遗传史", hypertensionFamilyHistoryCount);
        historyCounts.put("吸烟史", smokingHistoryCount);
        historyCounts.put("饮酒史", drinkingHistoryCount);
        historyCounts.put("传染病史", infectiousHistoryCount);
        historyCounts.put("食物过敏史", foodAllergyHistoryCount);
        historyCounts.put("高血压家族遗传史比例", df.format((double) hypertensionFamilyHistoryCount / total * 100));
        historyCounts.put("吸烟史比例", df.format((double) smokingHistoryCount / total * 100));
        historyCounts.put("饮酒史比例", df.format((double) drinkingHistoryCount / total * 100));
        historyCounts.put("传染病史比例", df.format((double) infectiousHistoryCount / total * 100));
        historyCounts.put("食物过敏史比例", df.format((double) foodAllergyHistoryCount / total * 100));

        JSONObject result = new JSONObject(historyCounts);
        return result;
    }

    @Override
    public JSONObject countPatientsHistory(Long doctorUid) {
        Map<String, Object> historyCounts = new LinkedHashMap<>();

        // 获取统计结果
        int hypertensionFamilyHistoryCount = countPatientsWithHypertensionFamilyHistory(doctorUid);
        int smokingHistoryCount = countPatientsWithSmokingHistory(doctorUid);
        int drinkingHistoryCount = countPatientsWithDrinkingHistory(doctorUid);
        int infectiousHistoryCount = countPatientsWithInfectiousHistory(doctorUid);
        int foodAllergyHistoryCount = countPatientsWithFoodAllergyHistory(doctorUid);
        int total = patientDoctorMapper.getDoctorCount(doctorUid);

        DecimalFormat df = new DecimalFormat("#.##");

        // 放入结果到Map中
        historyCounts.put("高血压家族遗传史", hypertensionFamilyHistoryCount);
        historyCounts.put("吸烟史", smokingHistoryCount);
        historyCounts.put("饮酒史", drinkingHistoryCount);
        historyCounts.put("传染病史", infectiousHistoryCount);
        historyCounts.put("食物过敏史", foodAllergyHistoryCount);
        historyCounts.put("高血压家族遗传史比例", df.format((double) hypertensionFamilyHistoryCount / total * 100));
        historyCounts.put("吸烟史比例", df.format((double) smokingHistoryCount / total * 100));
        historyCounts.put("饮酒史比例", df.format((double) drinkingHistoryCount / total * 100));
        historyCounts.put("传染病史比例", df.format((double) infectiousHistoryCount / total * 100));
        historyCounts.put("食物过敏史比例", df.format((double) foodAllergyHistoryCount / total * 100));

        JSONObject result = new JSONObject(historyCounts);
        return result;
    }*/
}