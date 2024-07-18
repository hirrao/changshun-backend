package com.pig4cloud.pig.doctor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.doctor.entity.DoctorBaseEntity;
import com.pig4cloud.pig.doctor.request.ImportDoctorBaseListRequest;
import java.util.List;
import org.springframework.validation.BindingResult;

public interface DoctorBaseService extends IService<DoctorBaseEntity> {
	
	R importDoctorBaseList(List<ImportDoctorBaseListRequest> excelVOList,
	 BindingResult bindingResult);

	boolean deleteBatch(List<Long> doctorUids);
	boolean updateBatch(List<DoctorBaseEntity> doctors);
	
}