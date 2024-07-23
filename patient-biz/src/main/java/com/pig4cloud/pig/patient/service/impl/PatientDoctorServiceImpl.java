package com.pig4cloud.pig.patient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.dto.HeartRateStatsDTO;
import com.pig4cloud.pig.patient.entity.PatientDoctorEntity;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import com.pig4cloud.pig.patient.mapper.PatientDoctorMapper;
import com.pig4cloud.pig.patient.mapper.PersureHeartRateMapper;
import com.pig4cloud.pig.patient.service.PatientDoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

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

	@Override
	public long countPatientsByDoctorId(Long doctorUid) {
		QueryWrapper<PatientDoctorEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("doctor_uid", doctorUid);
		return this.count(queryWrapper);
	}

	@Override
	public long countPatientsByDoctorIdAndCare(Long doctorUid) {
		QueryWrapper<PatientDoctorEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("doctor_uid", doctorUid).eq("care", 1);
		return this.count(queryWrapper);
	}
	@Autowired
	private PersureHeartRateMapper persureHeartRateMapper;

	@Override
	public long countBloodPressureRecordsByDoctorId(Long doctorUid) {
		// 获取昨天的开始和结束时间
		LocalDateTime startOfYesterday = LocalDateTime.now(ZoneId.of("UTC")).minusDays(1).toLocalDate().atStartOfDay();
		LocalDateTime endOfYesterday = startOfYesterday.plusDays(1);

		// 查询与医生绑定的患者UID
		QueryWrapper<PatientDoctorEntity> doctorQuery = new QueryWrapper<>();
		doctorQuery.eq("doctor_uid", doctorUid);

		List<Long> patientUids = this.list(doctorQuery).stream()
				.map(PatientDoctorEntity::getPatientUid)
				.collect(Collectors.toList());

		// 如果没有患者，直接返回0
		if (patientUids.isEmpty()) {
			return 0;
		}

		// 统计昨天的血压记录
		QueryWrapper<PersureHeartRateEntity> bpQuery = new QueryWrapper<>();
		bpQuery.in("patient_uid", patientUids)
				.ge("upload_time", startOfYesterday)
				.lt("upload_time", endOfYesterday);

		return persureHeartRateMapper.selectCount(bpQuery);
	}

	@Override
	public long countYesterdayAbnormalBloodPressureRecordsByDoctorId(Long doctorUid) {
		// 获取昨天的开始和结束时间
		LocalDateTime startOfYesterday = LocalDateTime.now(ZoneId.of("UTC")).minusDays(1).toLocalDate().atStartOfDay();
		LocalDateTime endOfYesterday = startOfYesterday.plusDays(1);

		// 查询与医生绑定的患者UID
		QueryWrapper<PatientDoctorEntity> doctorQuery = new QueryWrapper<>();
		doctorQuery.eq("doctor_uid", doctorUid);

		List<Long> patientUids = this.list(doctorQuery).stream()
				.map(PatientDoctorEntity::getPatientUid)
				.collect(Collectors.toList());

		// 如果没有患者，直接返回0
		if (patientUids.isEmpty()) {
			return 0;
		}

		// 统计昨天的异常血压记录（高压 > 140 或 低压 > 90）
		QueryWrapper<PersureHeartRateEntity> bpQuery = new QueryWrapper<>();
		bpQuery.in("patient_uid", patientUids)
				.between("upload_time", startOfYesterday, endOfYesterday)
				.and(wrapper -> wrapper
						.gt("systolic", 140)
						.or()
						.gt("diastolic", 90));

		return persureHeartRateMapper.selectCount(bpQuery);
	}
}