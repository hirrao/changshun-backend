package com.pig4cloud.pig.patient.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.entity.HeartRateLogsEntity;
import com.pig4cloud.pig.patient.entity.PatientDeviceEntity;
import com.pig4cloud.pig.patient.mapper.PatientDeviceMapper;
import com.pig4cloud.pig.patient.service.HeartRateLogsService;
import com.pig4cloud.pig.patient.service.PatientDeviceService;
import com.pig4cloud.pig.patient.utils.HTTPUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
	
	@Autowired
	private HeartRateLogsService heartRateLogsService;
	
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
	@Transactional(rollbackFor = Exception.class)
	@Override
	public R syncDeviceData(PatientDeviceEntity patientDevice) {
		//	查询从最后一次同步时间到当前时间的 血压数据，心率数据
		JSONObject data = new JSONObject();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		data.put("mac", patientDevice.getDeviceUid());
		data.put("start_time", dtf.format(patientDevice.getLastUpdateTime()));
		data.put("end_time", dtf.format(LocalDateTime.now()));
		//	 获取血压数据 
		try {
			JSONObject post = httpUtils.post(hardwareUrl + "/getblood", data);
			JSONArray dataList = post.getJSONArray("data");
			if (dataList != null) {
				//	TODO: 插入血压数据库，这部分需要等黄青修改完
				
			}
		} catch (Exception e) {
			// 手动强制回滚事务，这里一定要第一时间处理
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			log.error("同步血压数据失败", e);
			return R.failed("同步血压数据失败");
		}
		
		//  插入心率数据库中，完成批量查询操作
		try {
			JSONObject post = httpUtils.post(hardwareUrl + "/getheartrate", data);
			JSONArray dataList = post.getJSONArray("data");
			if (dataList != null) {
				// 批量查询心率数据，插入数据库
				// 重构数据
				List<HeartRateLogsEntity> heartRateLogsEntities = new ArrayList<>();
				for (int i = 0; i < dataList.size(); i++) {
					JSONObject jsonObjectTmp = dataList.getJSONObject(i);
					HeartRateLogsEntity tmp = new HeartRateLogsEntity();
					tmp.setPatientUid(patientDevice.getPatientUid());
					tmp.setHeartRate(jsonObjectTmp.getJSONObject("Hr").getInteger("Int64"));
					// note: 添加特定占位符使用‘x’指定字符串即可
					DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
					tmp.setUploadTime(LocalDateTime.parse(jsonObjectTmp.getJSONObject(
					 "Time").getString("Time"), df));
					heartRateLogsEntities.add(tmp);
				}
				heartRateLogsService.saveBatch(heartRateLogsEntities);
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			log.error("同步心率数据失败", e);
			return R.failed("同步心率数据失败");
		}
		return R.ok();
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