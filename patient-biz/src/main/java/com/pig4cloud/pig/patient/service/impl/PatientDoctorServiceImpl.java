package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.dto.HeartRateStatsDTO;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;
import com.pig4cloud.pig.patient.mapper.PatientDoctorMapper;
import com.pig4cloud.pig.patient.service.PatientDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 医患绑定表
 *
 * @author wangwenche
 * @date 2024-07-08 23:56:30
 */
@Service
public class PatientDoctorServiceImpl extends
 ServiceImpl<PatientDoctorMapper, PatientDoctorEntity> implements PatientDoctorService {
	
	private final PatientDoctorMapper patientDoctorMapper;
	
	@Autowired
	public PatientDoctorServiceImpl(PatientDoctorMapper patientDoctorMapper) {
		this.patientDoctorMapper = patientDoctorMapper;
	}
	
	@Override
	public HeartRateStatsDTO getHeartRateStats(Long doctorUid) {
		return patientDoctorMapper.getHeartRateStats(doctorUid);
	}
	
	@Override
	public R addItem(PatientDoctorEntity patientDoctor) {
		// 检查医生和患者是否已经绑定
		LambdaQueryWrapper<PatientDoctorEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(PatientDoctorEntity::getDoctorUid, patientDoctor.getDoctorUid());
		wrapper.eq(PatientDoctorEntity::getPatientUid, patientDoctor.getPatientUid());
		PatientDoctorEntity one = this.getOne(wrapper);
		if (one != null) {
			return R.failed("医生和患者已经绑定");
		}
		this.save(patientDoctor);
		return R.ok("绑定成功");
	}
	
	@Override
	public R updateItem(PatientDoctorEntity patientDoctor) {
		// 根据医患绑定关系更新特别关心
		LambdaQueryWrapper<PatientDoctorEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(PatientDoctorEntity::getDoctorUid, patientDoctor.getDoctorUid());
		wrapper.eq(PatientDoctorEntity::getPatientUid, patientDoctor.getPatientUid());
		PatientDoctorEntity one = this.getOne(wrapper);
		if (one != null) {
			one.setCare(patientDoctor.getCare());
			this.updateById(one);
			return R.ok("更新成功");
		}
		return R.failed("医生和患者已经绑定");
	}
}