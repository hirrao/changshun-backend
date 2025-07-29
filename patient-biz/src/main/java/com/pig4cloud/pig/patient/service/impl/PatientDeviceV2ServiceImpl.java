package com.pig4cloud.pig.patient.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.patient.entity.HeartRateLogsEntity;
import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.pig.patient.entity.PatientDeviceEntity;
import com.pig4cloud.pig.patient.entity.PersureHeartRateEntity;
import com.pig4cloud.pig.patient.mapper.HeartRateLogsMapper;
import com.pig4cloud.pig.patient.mapper.PatientBaseMapper;
import com.pig4cloud.pig.patient.mapper.PatientDeviceMapper;
import com.pig4cloud.pig.patient.mapper.PersureHeartRateMapper;
import com.pig4cloud.pig.patient.request.PatientDeviceCallbackRequest.BloodPressureCallback;
import com.pig4cloud.pig.patient.request.PatientDeviceCallbackRequest.HeartRateCallback;
import com.pig4cloud.pig.patient.request.PatientDeviceCallbackRequest.IPatientDeviceCallback;
import com.pig4cloud.pig.patient.service.PatientDeviceV2Service;
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
                                      HTTPUtils httpUtils,
                                      @Value("${hardware.appid}") String appid,
                                      @Value("${hardware.sec}") String secret,
                                      @Value("${hardware.callback_url}") String callbackUrl) {
        this.patientDeviceMapper = patientDeviceMapper;
        this.patientBaseMapper = patientBaseMapper;
        this.persureHeartRateMapper = persureHeartRateMapper;
        this.heartRateLogsMapper = heartRateLogsMapper;
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

    private void registerCallBack() {
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("access_token", token);
        Map<String, Object> params = new HashMap<>();
        params.put("url", callbackUrl);
        JSONObject jsonObject = httpUtils.post(
                "https://open.heart-forever.com/api/ext/callbackUrl", params,
                header);
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

    @Override
    @Transactional
    public R addPatientDevice(long uid, int weight, int height) {
        PatientDeviceEntity device = new PatientDeviceEntity();
        PatientBaseEntity user = patientBaseMapper.selectById(uid);
        if (user == null) {
            return R.failed("用户不存在");
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
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("access_token", token);
        Map<String, Object> params = new HashMap<>();
        params.put("extUserId", String.valueOf(device.getPddId()));
        params.put("birthday", user.getBirthday()
                                   .format(DateTimeFormatter.ISO_LOCAL_DATE));
        params.put("weight", String.valueOf(weight));
        params.put("height", String.valueOf(height));
        params.put("sex", user.getSex()
                              .equals("男性") ? "m" : "f");
        params.put("nickname", user.getUsername());
        JSONObject jsonObject = httpUtils.post(
                "https://open.heart-forever.com/api/ext/extUser", params,
                header);
        if (jsonObject.getInteger("code")
                      .equals(0)) {
            return R.ok();
        }
        else {
            TransactionAspectSupport.currentTransactionStatus()
                                    .setRollbackOnly();
            return R.failed("绑定设备失败: " + jsonObject.getString("message"));
        }
    }

    @Override
    public R bindPatientDevice(String imei, long uid) {
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
            return R.failed("未注册");
        }
        device.setDeviceUid(imei);
        device.setLastUpdateTime(LocalDateTime.now());
        return R.ok();
    }

    @Override
    public R generateUserAuthToken(long uid) {
        PatientDeviceEntity device = patientDeviceMapper.selectOne(
                new LambdaQueryWrapper<PatientDeviceEntity>().eq(
                        PatientDeviceEntity::getPatientUid, uid));
        if (device == null) {
            return R.failed("未注册");
        }
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("access_token", token);
        Map<String, Object> params = new HashMap<>();
        params.put("extUserId", String.valueOf(device.getPddId()));
        JSONObject jsonObject = httpUtils.post(
                "https://open.heart-forever.com/api/ext/getUserToken", params,
                header);
        if (jsonObject.getInteger("code")
                      .equals(0)) {
            return R.ok(jsonObject.getString("data"));
        }
        else {
            return R.failed(
                    "获取用户Token失败: " + jsonObject.getString("message"));
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

    private R handlePressureHeartRateEvent(float systolic, float diastolic,
                                           int heartRate, Long pddId,
                                           Integer timestamp) {
        PatientDeviceEntity device = patientDeviceMapper.selectById(pddId);
        if (device == null) {
            return R.failed();
        }
        long uid = device.getPatientUid();
        LocalDateTime time = LocalDateTime.ofEpochSecond(timestamp, 0,
                                                         ZoneOffset.UTC);
        PersureHeartRateEntity pressure = new PersureHeartRateEntity();
        pressure.setPatientUid(uid);
        pressure.setDiastolic(diastolic);
        pressure.setSystolic(systolic);
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
                                                         ZoneOffset.UTC);
        HeartRateLogsEntity heartRateLogs = new HeartRateLogsEntity();
        heartRateLogs.setPatientUid(uid);
        heartRateLogs.setHeartRate(heartRate);
        heartRateLogs.setUploadTime(time);
        heartRateLogsMapper.insert(heartRateLogs);
        return R.ok();
    }
}
