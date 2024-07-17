package com.pig4cloud.pig.patient.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.patient.dto.PatientiListDTO;
import com.pig4cloud.pig.patient.mapper.PersureHeartRateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <h3>patient</h3>
 *
 * @author HuangJiayu
 * @description <p>患者列表服务类</p>
 * @date 2024-07-17 15:30
 **/
@Service
public class PatientListService {
	
	@Autowired
	private PersureHeartRateMapper persureHeartRateMapper;
	
	//	分页查询患者列表，需要结合患者基本信息，患者病情，患者血压心率记录
	public Page<PatientiListDTO> getPatientList(Page page, PatientiListDTO patientiListDTO) {
		return persureHeartRateMapper.selectPatientList(page,patientiListDTO);
	}
}
