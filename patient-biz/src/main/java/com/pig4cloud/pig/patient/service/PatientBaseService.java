package com.pig4cloud.pig.patient.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.pig.patient.request.ImportPatientBaseListRequest;
import java.util.List;
import org.springframework.validation.BindingResult;

public interface PatientBaseService extends IService<PatientBaseEntity> {
    // 分页查询并按care字段降序排序
    IPage<PatientBaseEntity> pageByCare(Page<?> page);
    int countMalePatientsOver55(Long doctorUid);

    int countMalePatientsUnderEqual55(Long doctorUid);

    int countFemalePatientsOver65(Long doctorUid);

    int countFemalePatientsUnderEqual66(Long doctorUid);

    int ccountMalePatientsOver55(Long doctorUid);

    int ccountMalePatientsUnderEqual55(Long doctorUid);

    int ccountFemalePatientsOver65(Long doctorUid);

    int ccountFemalePatientsUnderEqual66(Long doctorUid);
    
    R importPatientBaseList(List<ImportPatientBaseListRequest> excelVOList,
     BindingResult bindingResult);

    JSONObject getPatientStatistics(Long doctorUid);
    JSONObject getPatientbycareStatistics(Long doctorUid);

}