package com.pig4cloud.pig.patient.controller;

import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.pig.patient.request.PatientDeviceBindRequest;
import com.pig4cloud.pig.patient.request.PatientDeviceCallbackRequest.PatientDeviceCallbackRequest;
import com.pig4cloud.pig.patient.request.PatientDeviceUpdateRequest;
import com.pig4cloud.pig.patient.service.PatientDeviceV2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/***
 * @author wangyifei
 * 心永手表对接, 替代原有接口
 * @see PatientDeviceController
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/patientDeviceV2")
@Tag(description = "patientDeviceV2", name = "管理设备")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PatientDeviceV2Controller {
    private final PatientDeviceV2Service patientDeviceV2Service;

    @Operation(summary = "绑定设备", description = "绑定设备")
    @SysLog("绑定设备")
    @PostMapping("/bind_device")
    @PreAuthorize("@pms.hasPermission('patient_patientDevice_add')")
    public R bindDevice(@RequestBody PatientDeviceBindRequest request) {
        return patientDeviceV2Service.bindPatientDevice(request.getImei(),
                                                        request.getUid());
    }

    @Operation(summary = "更新设备", description = "更新设备")
    @SysLog("更新设备")
    @PostMapping("/update_device")
    @PreAuthorize("@pms.hasPermission('patient_patientDevice_add')")
    public R updateDevice(@RequestBody PatientDeviceUpdateRequest request) {
        return patientDeviceV2Service.updatePatientDevice(request.getUid(),
                                                          request.getHeight(),
                                                          request.getWeight());
    }

    @Operation(summary = "解绑设备", description = "解绑设备")
    @SysLog("解绑设备")
    @PostMapping("/unbind_device")
    @PreAuthorize("@pms.hasPermission('patient_patientDevice_add')")
    public R unbindDevice(@RequestBody PatientDeviceBindRequest request) {
        return patientDeviceV2Service.unbindPatientDevice(request.getUid());
    }

    @Operation(summary = "获取User Token", description = "获取User Token")
    @SysLog("获取Token")
    @GetMapping("/refresh_token/{uid}")
    public R refreshToken(@PathVariable Long uid) {
        Object obj = patientDeviceV2Service.generateUserAuthToken(uid);
        if (obj instanceof String str) {
            return R.ok(str);
        }
        if (obj instanceof R r) {
            return r;
        }
        return R.failed(obj, "未知错误");
    }

    @Operation(summary = "通过id查询", description = "通过id查询")
    @GetMapping("/{uid}")
    @PreAuthorize("@pms.hasPermission('patient_patientDevice_view')")
    public R getById(@PathVariable Long uid) {
        return R.ok(patientDeviceV2Service.getByUid(uid));
    }

    @Operation(summary = "手表回调接口", description = "手表回调接口")
    @PostMapping("/callback")
    @Inner(value = false)
    public R callback(@RequestBody PatientDeviceCallbackRequest request) {
        return patientDeviceV2Service.callback(request.getEventType(),
                                               request.getUserId(),
                                               request.getTimestamp(),
                                               request.getEventData());
    }
}
