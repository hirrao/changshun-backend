package com.pig4cloud.pig.patient.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.entity.*;
import com.pig4cloud.pig.patient.mapper.*;
import com.pig4cloud.pig.patient.request.PatientDeviceCallbackRequest.BloodPressureCallback;
import com.pig4cloud.pig.patient.request.PatientDeviceCallbackRequest.HeartRateCallback;
import com.pig4cloud.pig.patient.request.PatientDeviceCallbackRequest.IPatientDeviceCallback;
import com.pig4cloud.pig.patient.service.PatientDeviceV2Service;
import com.pig4cloud.pig.patient.service.PersureHeartRateService;
import com.pig4cloud.pig.patient.utils.HTTPUtils;
import com.pig4cloud.pig.patient.utils.SignUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PatientDeviceV2ServiceImpl extends ServiceImpl<PatientDeviceMapper, PatientDeviceEntity> implements PatientDeviceV2Service {
    private final PatientDeviceMapper patientDeviceMapper;
    private final PatientBaseMapper patientBaseMapper;
    private final PersureHeartRateMapper persureHeartRateMapper;
    private final HeartRateLogsMapper heartRateLogsMapper;
    private final PatientBmiManaMapper patientBmiManaMapper;
    private final PersureHeartRateService persureHeartRateService;
    private final HTTPUtils httpUtils;
    private final String appid;
    private final String secret;
    private final String callbackUrl;
    private String token;

    @Autowired
    public PatientDeviceV2ServiceImpl(PatientDeviceMapper patientDeviceMapper,
                                      PatientBaseMapper patientBaseMapper,
                                      PersureHeartRateMapper persureHeartRateMapper,
                                      HeartRateLogsMapper heartRateLogsMapper,
                                      PatientBmiManaMapper patientBmiManaMapper,
                                      PersureHeartRateService persureHeartRateService,
                                      HTTPUtils httpUtils,
                                      @Value("${hardware.appid}") String appid,
                                      @Value("${hardware.sec}") String secret,
                                      @Value("${hardware.callback_url}") String callbackUrl) {
        this.patientDeviceMapper = patientDeviceMapper;
        this.patientBaseMapper = patientBaseMapper;
        this.persureHeartRateMapper = persureHeartRateMapper;
        this.heartRateLogsMapper = heartRateLogsMapper;
        this.patientBmiManaMapper = patientBmiManaMapper;
        this.persureHeartRateService = persureHeartRateService;
        this.httpUtils = httpUtils;
        this.appid = appid;
        this.secret = secret;
        this.callbackUrl = callbackUrl;
    }

    @PostConstruct
    public void init() {
        this.token = getAuthToken();
        registerCallBack();
    }

    private JSONObject post(String url, Map<String, Object> params) {
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("access_token", token);
        JSONObject response = httpUtils.post(url, params, header);
        if (response.getInteger("code") == 10007 || response.getInteger(
                "code") == 10020) {
            token = getAuthToken();
            log.warn("token失效刷新");
            // 刷新token后重新请求
            MultiValueMap<String, String> newHeader = new LinkedMultiValueMap<>();
            newHeader.add("access_token", token);
            response = httpUtils.post(url, params, newHeader);
        }
        return response;
    }

    private void registerCallBack() {
        Map<String, Object> params = new HashMap<>();
        params.put("url", callbackUrl);
        JSONObject jsonObject = post(
                "https://open.heart-forever.com/api/ext/callbackUrl", params);
        if (jsonObject.getInteger("code") != 0) {
            throw new RuntimeException("回调接口注册错误");
        }
    }

    private String getAuthToken() {
        Map<String, Object> params = new HashMap<>();
        String nonce = UUID.randomUUID()
                           .toString();
        long timestamp = System.currentTimeMillis();
        params.put("appid", appid);
        params.put("signature",
                   SignUtils.sign(appid, nonce, secret, timestamp));
        params.put("nonce", nonce);
        params.put("timestamp", String.valueOf(timestamp));
        JSONObject jsonObject = httpUtils.post(
                "https://open.heart-forever.com/api/ext/getToken", params);
        if (jsonObject.getInteger("code")
                      .equals(0)) {
            return jsonObject.getString("data");
        }
        else {
            throw new RuntimeException(
                    "获取设备授权失败: " + jsonObject.getString("message"));
        }
    }


    @Override
    public R getByUid(long uid) {
        PatientDeviceEntity entity = patientDeviceMapper.selectOne(
                new LambdaQueryWrapper<PatientDeviceEntity>().eq(
                        PatientDeviceEntity::getPatientUid, uid));
        if (entity == null) {
            return R.failed("未注册");
        }
        return R.ok(entity);
    }

    public Object addPatientDevice(long uid) {
        PatientDeviceEntity device = new PatientDeviceEntity();
        PatientBmiManaEntity bmiMana = patientBmiManaMapper.selectOne(
                new LambdaQueryWrapper<PatientBmiManaEntity>().eq(
                                                                      PatientBmiManaEntity::getPatientUid, uid)
                                                              .orderByDesc(
                                                                      PatientBmiManaEntity::getBmimeasurementDate)
                                                              .last("LIMIT 1"));
        PatientBaseEntity user = patientBaseMapper.selectById(uid);
        if (user == null) {
            return "用户不存在";
        }
        if (bmiMana == null) {
            return "未查询到BMI记录";
        }
        PatientDeviceEntity entity = patientDeviceMapper.selectOne(
                new LambdaQueryWrapper<PatientDeviceEntity>().eq(
                        PatientDeviceEntity::getPatientUid, uid));
        if (entity == null) {
            device.setPatientUid(uid);
            device.setLastUpdateTime(LocalDateTime.now());
            save(device);
        }
        else {
            device = entity;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("extUserId", String.valueOf(device.getPddId()));
        params.put("birthday", user.getBirthday()
                                   .format(DateTimeFormatter.ISO_LOCAL_DATE));
        params.put("weight", String.valueOf(bmiMana.getWeight()
                                                   .intValue()));
        params.put("height", String.valueOf(bmiMana.getHeight()
                                                   .intValue()));
        params.put("sex", user.getSex()
                              .equals("男性") ? "m" : "f");
        params.put("nickname", user.getUsername());
        JSONObject jsonObject = post(
                "https://open.heart-forever.com/api/ext/extUser", params);
        if (jsonObject.getInteger("code")
                      .equals(0)) {
            return Boolean.TRUE;
        }
        else {
            TransactionAspectSupport.currentTransactionStatus()
                                    .setRollbackOnly();
            return jsonObject;
        }
    }

    @Override
    @Transactional
    public R bindPatientDevice(String imei, long uid) {
        if (!StringUtils.hasText(imei)) {
            return R.failed("无效的Imei");
        }
        PatientDeviceEntity entity = patientDeviceMapper.selectOne(
                new LambdaQueryWrapper<PatientDeviceEntity>().eq(
                        PatientDeviceEntity::getDeviceUid, imei));
        if (entity != null) {
            return R.failed("设备已被其他人注册");
        }
        PatientDeviceEntity device = patientDeviceMapper.selectOne(
                new LambdaQueryWrapper<PatientDeviceEntity>().eq(
                        PatientDeviceEntity::getPatientUid, uid));
        if (device == null) {
            Object response = addPatientDevice(uid);
            if (!Boolean.TRUE.equals(response)) {
                return R.failed(response, "注册失败, 请稍后重试");
            }
            device = patientDeviceMapper.selectOne(
                    new LambdaQueryWrapper<PatientDeviceEntity>().eq(
                            PatientDeviceEntity::getPatientUid, uid));
        }
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        Object obj = generateUserAuthToken(uid);
        if (obj instanceof String userAccessToken) {
            header.add("user_access_token", userAccessToken);
        }
        else {
            return (R) obj;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("imei", imei);
        JSONObject jsonObject = httpUtils.post(
                "https://open.heart-forever.com/api/ext/binding", params,
                header);
        if (jsonObject.getInteger("code") != 0) {
            return R.failed(jsonObject, "绑定设备失败");
        }
        device.setDeviceUid(imei);
        device.setDeviceBrand("xy");
        device.setLastUpdateTime(LocalDateTime.now());
        patientDeviceMapper.updateById(device);
        return R.ok();
    }

    public Object generateUserAuthToken(long uid) {
        PatientDeviceEntity device = patientDeviceMapper.selectOne(
                new LambdaQueryWrapper<PatientDeviceEntity>().eq(
                        PatientDeviceEntity::getPatientUid, uid));
        if (device == null) {
            return R.failed("未注册");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("extUserId", String.valueOf(device.getPddId()));
        JSONObject jsonObject = post(
                "https://open.heart-forever.com/api/ext/getUserToken", params);
        if (jsonObject.getInteger("code")
                      .equals(0)) {
            return jsonObject.getString("data");
        }
        else {
            return R.failed(jsonObject, "获取用户授权失败");
        }
    }

    @Override
    @Transactional
    public R callback(String eventType, Long pddId, Integer timestamp,
                      IPatientDeviceCallback eventData) {
        return switch (eventType) {
            case "bp_event" -> {
                if (eventData instanceof BloodPressureCallback request) {
                    yield handlePressureHeartRateEvent(request.getSbp(),
                                                       request.getDbp(),
                                                       request.getHr(), pddId,
                                                       timestamp);
                }
                yield R.failed();
            }
            case "hr_event" -> {
                if (eventData instanceof HeartRateCallback request) {
                    yield handleHeartRateEvent(request.getValue(), pddId,
                                               timestamp);
                }
                yield R.failed();
            }
            default -> R.ok();
        };
    }

    @Override
    public R unbindPatientDevice(Long uid) {
        PatientDeviceEntity device = patientDeviceMapper.selectOne(
                new LambdaQueryWrapper<PatientDeviceEntity>().eq(
                        PatientDeviceEntity::getPatientUid, uid));
        if (device == null || device.getDeviceUid() == null) {
            return R.failed("用户未注册");
        }
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        Object obj = generateUserAuthToken(uid);
        if (obj instanceof String userAccessToken) {
            header.add("user_access_token", userAccessToken);
        }
        else {
            return (R) obj;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("imei", device.getDeviceUid());
        JSONObject jsonObject = httpUtils.post(
                "https://open.heart-forever.com/api/ext/unbinding", params,
                header);
        if (jsonObject.getInteger("code") != 0) {
            return R.failed(jsonObject, "解绑设备失败");
        }
        device.setLastUpdateTime(LocalDateTime.now());
        patientDeviceMapper.update(device,
                                   new LambdaUpdateWrapper<>(device).eq(
                                                                                         PatientDeviceEntity::getPatientUid,
                                                                                         uid)
                                                                    .set(PatientDeviceEntity::getDeviceBrand,
                                                                                      null)
                                                                    .set(PatientDeviceEntity::getDeviceUid,
                                                                                      null));
        return R.ok("解绑成功");
    }

    private R handlePressureHeartRateEvent(float systolic, float diastolic,
                                           int heartRate, Long pddId,
                                           Integer timestamp) {
        PatientDeviceEntity device = patientDeviceMapper.selectById(pddId);
        if (device == null) {
            return R.failed();
        }
        long uid = device.getPatientUid();
        LocalDateTime time = LocalDateTime.ofEpochSecond(timestamp, 0,
                                                         ZoneOffset.ofHours(8));
        PersureHeartRateEntity pressure = new PersureHeartRateEntity();
        pressure.setPatientUid(uid);
        pressure.setDiastolic(diastolic);
        pressure.setSystolic(systolic);
        pressure = persureHeartRateService.setSdhAndRiskClass(pressure);
        pressure.setUploadTime(time);
        persureHeartRateMapper.insert(pressure);
        HeartRateLogsEntity heartRateLogs = new HeartRateLogsEntity();
        heartRateLogs.setPatientUid(uid);
        heartRateLogs.setHeartRate(heartRate);
        heartRateLogs.setUploadTime(time);
        heartRateLogsMapper.insert(heartRateLogs);
        return R.ok();
    }

    private R handleHeartRateEvent(int heartRate, Long pddId,
                                   Integer timestamp) {
        PatientDeviceEntity device = patientDeviceMapper.selectById(pddId);
        if (device == null) {
            return R.failed();
        }
        long uid = device.getPatientUid();
        LocalDateTime time = LocalDateTime.ofEpochSecond(timestamp, 0,
                                                         ZoneOffset.ofHours(8));
        HeartRateLogsEntity heartRateLogs = new HeartRateLogsEntity();
        heartRateLogs.setPatientUid(uid);
        heartRateLogs.setHeartRate(heartRate);
        heartRateLogs.setUploadTime(time);
        heartRateLogsMapper.insert(heartRateLogs);
        return R.ok();
    }
}
