package com.pig4cloud.pig.patient.utils;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author wangyifei
 * 生成心永签名字符串
 **/
public class SignUtils {
    public static String sign(String appId, String nonce, String secretKey,
                              long timestamp) {
        String str = String.format(
                "appid=%s&nonce=%s&secretKey=%s&timestamp=%d", appId, nonce,
                secretKey, timestamp);
        return DigestUtils.md5Hex(str);
    }
}
