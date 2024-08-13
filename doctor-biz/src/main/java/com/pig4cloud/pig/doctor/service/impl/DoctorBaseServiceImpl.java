package com.pig4cloud.pig.doctor.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.doctor.entity.DoctorBaseEntity;
import com.pig4cloud.pig.doctor.mapper.DoctorBaseMapper;
import com.pig4cloud.pig.doctor.request.ImportDoctorBaseListRequest;
import com.pig4cloud.pig.doctor.service.DoctorBaseService;
import com.pig4cloud.pig.doctor.utils.HTTPUtils;
import com.pig4cloud.plugin.excel.vo.ErrorMessage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

/**
 * 医生基础信息管理
 *
 * @author huangjiayu
 * @date 2024-07-03 17:17:03
 */
@Service
@Slf4j
public class DoctorBaseServiceImpl extends
 ServiceImpl<DoctorBaseMapper, DoctorBaseEntity> implements DoctorBaseService {
	@Value("${web.patient-url}")
	private String patientUrlPrefix;
	
	@Autowired
	private HTTPUtils httpUtils;
	
	@Override
	public R importDoctorBaseList(List<ImportDoctorBaseListRequest> excelVOList,
	 BindingResult bindingResult) {
		// 通用校验获取失败的数据
		List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();
		
		for (ImportDoctorBaseListRequest importDoctorBaseListRequest : excelVOList) {
			
			// 校验插入数据是否合法
			
			// 依靠手机号验证，这样如果用户在医院那里变更系统仍然可以添加到数据库
			
			DoctorBaseEntity tmp = new DoctorBaseEntity();
			tmp.setDoctorPhonenumber(importDoctorBaseListRequest.getDoctorPhonenumber());
			LambdaQueryWrapper<DoctorBaseEntity> wrapper = Wrappers.lambdaQuery();
			wrapper.setEntity(tmp);
			DoctorBaseEntity one = this.getOne(wrapper);
			
			Set<String> errorMsg = new HashSet<>();
			if (one != null) {
				errorMsg.add(
				 "电话号:" + importDoctorBaseListRequest.getDoctorPhonenumber() + "已存在");
			}
			
			//	数据合法
			if (CollUtil.isEmpty(errorMsg)) {
				tmp.setDoctorName(importDoctorBaseListRequest.getDoctorName());
				tmp.setPosition(importDoctorBaseListRequest.getPosition());
				tmp.setAffiliatedHospital(importDoctorBaseListRequest.getAffiliatedHospital());
				tmp.setDepartment(importDoctorBaseListRequest.getDepartment());
				tmp.setUsername(importDoctorBaseListRequest.getUsername());
				
				//	插入数据库
				this.save(tmp);
			} else {
				//	数据不合法
				errorMessageList.add(
				 new ErrorMessage(importDoctorBaseListRequest.getLineNum(), errorMsg));
			}
			
		}
		
		if (CollUtil.isNotEmpty(errorMessageList)) {
			return R.failed(errorMessageList);
		}
		return R.ok();
	}


	@Override
	public boolean deleteBatch(List<Long> doctorUids) {
		// 先删除医患绑定表
		String url =  patientUrlPrefix + "/patientDoctor/delete_patient_doctor";
		for (Long l : doctorUids) {
			Map<String, Object> params = new HashMap<>();
			params.put("doctorUid", l);
			try {
				JSONObject post = httpUtils.post(url, params);
			} catch (Exception e) {
				log.error("請求失敗",e);
				return false;
			}
		}
		return this.removeByIds(doctorUids);
	}

	@Override
	public boolean updateBatch(List<DoctorBaseEntity> doctors) {
		return this.updateBatchById(doctors);
	}
	
}