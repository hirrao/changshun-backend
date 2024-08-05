package com.pig4cloud.pig.patient.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import com.pig4cloud.pig.patient.mapper.PatientBaseMapper;
import com.pig4cloud.pig.patient.mapper.PatientDoctorMapper;
import com.pig4cloud.pig.patient.request.ImportPatientBaseListRequest;
import com.pig4cloud.pig.patient.service.PatientBaseService;
import com.pig4cloud.plugin.excel.vo.ErrorMessage;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

/**
 * 患者基本信息
 *
 * @author huangjiayu
 * @date 2024-07-03 16:07:48
 */
@Service
public class PatientBaseServiceImpl extends
 ServiceImpl<PatientBaseMapper, PatientBaseEntity> implements PatientBaseService {
	
	private final PatientBaseMapper patientBaseMapper;
	
	public PatientBaseServiceImpl(PatientBaseMapper patientBaseMapper) {
		this.patientBaseMapper = patientBaseMapper;
	}
	
	@Override
	public IPage<PatientBaseEntity> pageByCare(Page<?> page) {
		return patientBaseMapper.selectPatientBasePageByCare(page);
	}
	
	@Autowired
	private PatientDoctorMapper patientDoctorMapper;
	
	@Override
	public int countMalePatientsOver55(Long doctorUid) {
		return patientBaseMapper.countMalePatientsOver55(doctorUid);
	}
	
	@Override
	public int countMalePatientsUnderEqual55(Long doctorUid) {
		return patientBaseMapper.countMalePatientsUnderEqual55(doctorUid);
	}
	
	@Override
	public int countFemalePatientsOver65(Long doctorUid) {
		return patientBaseMapper.countFemalePatientsOver65(doctorUid);
	}
	
	@Override
	public int countFemalePatientsUnderEqual66(Long doctorUid) {
		return patientBaseMapper.countFemalePatientsUnderEqual66(doctorUid);
	}
	
	@Override
	public int ccountMalePatientsOver55(Long doctorUid) {
		return patientBaseMapper.ccountMalePatientsOver55(doctorUid);
	}
	
	@Override
	public int ccountMalePatientsUnderEqual55(Long doctorUid) {
		return patientBaseMapper.ccountMalePatientsUnderEqual55(doctorUid);
	}
	
	@Override
	public int ccountFemalePatientsOver65(Long doctorUid) {
		return patientBaseMapper.ccountFemalePatientsOver65(doctorUid);
	}
	
	@Override
	public int ccountFemalePatientsUnderEqual66(Long doctorUid) {
		return patientBaseMapper.ccountFemalePatientsUnderEqual66(doctorUid);
	}
	
	
	@Override
	public R importPatientBaseList(List<ImportPatientBaseListRequest> excelVOList,
	 BindingResult bindingResult) {
		// 通用校验获取失败的数据
		List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();
		
		//	执行数据插入操作
		for (ImportPatientBaseListRequest importPatientBaseListRequest : excelVOList) {
			// 校验插入数据是否合法
			
			// 依靠手机号验证，这样如果用户在医院那里变更系统仍然可以添加到数据库
			PatientBaseEntity tmp = new PatientBaseEntity();
			tmp.setPhoneNumber(importPatientBaseListRequest.getPhoneNumber());
			LambdaQueryWrapper<PatientBaseEntity> wrapper = Wrappers.lambdaQuery();
			wrapper.setEntity(tmp);
			PatientBaseEntity one = this.getOne(wrapper);
			
			Set<String> errorMsg = new HashSet<>();
			if (one != null) {
				errorMsg.add("电话号:" + importPatientBaseListRequest.getPhoneNumber() + "已存在");
			}
			
			// 数据合法
			if (CollUtil.isEmpty(errorMsg)) {
				tmp.setIdentificationNumber(importPatientBaseListRequest.getIdentificationNumber());
				tmp.setPatientName(importPatientBaseListRequest.getPatientName());
				tmp.setSex(importPatientBaseListRequest.getSex());
				tmp.setBirthday(importPatientBaseListRequest.getBirthday());
				tmp.setUsername(importPatientBaseListRequest.getUsername());
				
				//	插入数据
				this.save(tmp);
			} else {
				//	数据不合法
				errorMessageList.add(
				 new ErrorMessage(importPatientBaseListRequest.getLineNum(), errorMsg));
			}
		}
		
		if (CollUtil.isNotEmpty(errorMessageList)) {
			return R.failed(errorMessageList);
		}
		
		return R.ok();
	}

	@Override
	public JSONObject getPatientStatistics(Long doctorUid) {
		Map<String, Object> mapData = new LinkedHashMap<>();
		mapData.put("male1", countMalePatientsOver55(doctorUid));
		mapData.put("male2", countMalePatientsUnderEqual55(doctorUid));
		mapData.put("female1", countFemalePatientsOver65(doctorUid));
		mapData.put("female2", countFemalePatientsUnderEqual66(doctorUid));
		JSONObject data = new JSONObject(mapData);
		return data;
	}

	@Override
	public JSONObject getPatientbycareStatistics(Long doctorUid) {
		Map<String, Object> mapData = new LinkedHashMap<>();
		mapData.put("male1", ccountMalePatientsOver55(doctorUid));
		mapData.put("male2", ccountMalePatientsUnderEqual55(doctorUid));
		mapData.put("female1", ccountFemalePatientsOver65(doctorUid));
		mapData.put("female2", ccountFemalePatientsUnderEqual66(doctorUid));
		JSONObject data = new JSONObject(mapData);
		return data;
	}

	@Override
	public String editPhysicalStrength(Long patientUid, int physicalStrength) {
		if (physicalStrength < 0 || physicalStrength > 4) {
			return "Physical strength must be between 0 and 4.";
		}

		LambdaQueryWrapper<PatientBaseEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(PatientBaseEntity::getPatientUid, patientUid).last("limit 1");

		PatientBaseEntity patient = patientBaseMapper.selectOne(queryWrapper);

		if (patient == null) {
			return "Patient not found with uid: " + patientUid;
		}

		patient.setPhysicalStrength(physicalStrength);
		patientBaseMapper.updateById(patient);

		return "success";
	}
}