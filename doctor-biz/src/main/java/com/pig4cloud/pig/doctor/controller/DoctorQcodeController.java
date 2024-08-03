package com.pig4cloud.pig.doctor.controller;

import com.alibaba.fastjson.JSONObject;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.pig.doctor.utils.QRCodeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.FileSupportConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h3>doctor</h3>
 *
 * @author HuangJiayu
 * @description <p>医生二维码控制类</p>
 * @date 2024-08-03 11:11
 **/

@RestController
@RequiredArgsConstructor
@RequestMapping("/qrcode")
@Tag(description = "QRcode", name = "医生Id二维码管理")
@Slf4j
public class DoctorQcodeController {
	
	@Value("${web.qrcode}")
	private String qrcodePath;
	
	
	@Inner(value = false)
	@Operation(summary = "生成医生二维码", description = "生成医生二维码")
	@PostMapping("/generate")
	public R generate(@RequestBody JSONObject jsonObject) {
		// 获取医生Uid
		Long doctorUid = jsonObject.getLong("doctorUid");
		// 验证静态资源是否存在
		String path = qrcodePath + '/' + doctorUid + ".png";
		File file = new File(path);
		if (file.exists()) {
			return R.ok(path);
		} 
		// 不存再生成
		try {
			QRCodeUtil.encode(doctorUid.toString(),qrcodePath,doctorUid);
			return R.ok(path);
		} catch (Exception e) {
			log.error("生成二维码错误",e);
			return R.failed("生成二维码错误");
		}
		
	}
}
