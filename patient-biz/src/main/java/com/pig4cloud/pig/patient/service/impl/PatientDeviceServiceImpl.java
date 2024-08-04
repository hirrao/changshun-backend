package com.pig4cloud.pig.patient.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.entity.PatientDeviceEntity;
import com.pig4cloud.pig.patient.mapper.PatientDeviceMapper;
import com.pig4cloud.pig.patient.service.PatientDeviceService;
import com.pig4cloud.pig.patient.utils.HTTPUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangwenche
 * @date 2024-07-04 20:47:58
 */
@Service
@Slf4j
public class PatientDeviceServiceImpl extends
 ServiceImpl<PatientDeviceMapper, PatientDeviceEntity> implements PatientDeviceService {
	
	@Value("${hardware.url}")
	private String hardwareUrl;
	
	@Autowired
	private HTTPUtils httpUtils;
	
	@Override
	public R addPatientDevice(PatientDeviceEntity patientDevice) {
		// 检测该用户是否已经绑定该设备
		LambdaQueryWrapper<PatientDeviceEntity> wrapper = new LambdaQueryWrapper<>();
		PatientDeviceEntity tmp = new PatientDeviceEntity();
		tmp.setDeviceUid(patientDevice.getDeviceUid());
		wrapper.setEntity(tmp);
		PatientDeviceEntity one = this.getOne(wrapper.last("limit 1"));
		if (one != null) {
			return R.failed("该设备正在被他人使用,无法绑定");
		}
		// 设置最后一次更新时间默认为当前时间
		patientDevice.setLastUpdateTime(LocalDateTime.now());
		return R.ok(this.save(patientDevice));
	}
	
	// 异常回滚事务
	@Transactional
	@Override
	public R syncDeviceData(PatientDeviceEntity patientDevice) {
		//	查询从最后一次同步时间到当前时间的 血压数据，心率数据
		JSONObject data = new JSONObject();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		data.put("mac", patientDevice.getDeviceUid());
		data.put("start_time", dtf.format(patientDevice.getLastUpdateTime()));
		data.put("end_time", dtf.format(LocalDateTime.now()));
		//	TODO: 获取血压数据 需要确认现在血压和心率是否需要分开
		try {
			JSONObject post = httpUtils.post(hardwareUrl + "/getblood", data);
			JSONArray dataList = post.getJSONArray("data");
			if (dataList != null) {
				//	插入血压数据库
					
			}
		} catch (Exception e) {
			log.error("同步血压数据失败", e);
			return R.failed("同步血压数据失败");
		}
		
		//  插入数据库中，完成保存
		
		return null;
	}
	
	@Override
	public R unbindDevice(PatientDeviceEntity patientDevice) {
		// 检测该用户是否已经绑定该设备
		LambdaQueryWrapper<PatientDeviceEntity> wrapper = new LambdaQueryWrapper<>();
		PatientDeviceEntity tmp = new PatientDeviceEntity();
		tmp.setDeviceUid(patientDevice.getDeviceUid());
		tmp.setPatientUid(patientDevice.getPatientUid());
		wrapper.setEntity(tmp);
		PatientDeviceEntity one = this.getOne(wrapper.last("limit 1"));
		if (one == null) {
			return R.failed("该设备未被绑定");
		}
		return R.ok(this.remove(wrapper));
	}
}