package com.pig4cloud.pig.patient.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.entity.PatientDeviceEntity;

public interface PatientDeviceService extends IService<PatientDeviceEntity> {
	// 添加患者设备
	R addPatientDevice(PatientDeviceEntity patientDevice);
	
	// 同步设备数据
	R syncDeviceData(PatientDeviceEntity patientDevice);
	
	// 解绑设备
	R unbindDevice(PatientDeviceEntity patientDevice);
}