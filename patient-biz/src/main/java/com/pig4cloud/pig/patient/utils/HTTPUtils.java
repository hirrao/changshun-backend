package com.pig4cloud.pig.patient.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

/**
 * <h3>patient</h3>
 *
 * @author HuangJiayu
 * @description <p>HTTP 请求类</p>
 * @date 2024-07-26 17:29
 **/
@Component
public class HTTPUtils {
	
	// 一般post请求使用该类封装，get请求直接使用原生restTemplate 即可
	
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * @param url:    访问地址
	 * @param params: 请求参数，前面是key，后面是value
	 * @return JSONObject: 通过JSONObject.getXX(key)获取value
	 * @date :2024/4/5 14:44
	 * @description : 发送post请求，该重载方法用于发送json格式的请求，适用于灵活添加参数，通过HashMap类型添加键值对，可以是数组等多种复杂类型
	 */
	
	public JSONObject post(String url, Map<String, Object> params, MediaType mediaType) {
		HttpHeaders headers = new HttpHeaders();
		// 默认是JSON格式
		if (mediaType != null) {
			headers.setContentType(mediaType);
		} else {
			headers.setContentType(MediaType.APPLICATION_JSON);
		}
		// SpringBoot3 需要变为字符串才行，不然会没有body
		HttpEntity<String> request = new HttpEntity<>(JSON.toJSONString(params), headers);
		ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, request,
		 JSONObject.class);
		if (response.getStatusCode() != HttpStatus.OK) {
			throw new ResponseStatusException(response.getStatusCode(), "请求出错");
		}
		return response.getBody();
	}
	
	public JSONObject post(String url, Map<String, Object> params) {
		HttpHeaders headers = new HttpHeaders();
		// 默认是JSON格式
		headers.setContentType(MediaType.APPLICATION_JSON);
		// SpringBoot3 需要变为字符串才行，不然会没有body
		HttpEntity<String> request = new HttpEntity<>(JSON.toJSONString(params), headers);
		ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, request,
		 JSONObject.class);
		if (response.getStatusCode() != HttpStatus.OK) {
			throw new ResponseStatusException(response.getStatusCode(), "请求出错");
		}
		return response.getBody();
	}

	public JSONObject post(String url, Map<String, Object> params,
						   MultiValueMap<String,String> headersMap) {
		HttpHeaders headers = new HttpHeaders(headersMap);
		// 默认是JSON格式
		headers.setContentType(MediaType.APPLICATION_JSON);
		// SpringBoot3 需要变为字符串才行，不然会没有body
		HttpEntity<String> request = new HttpEntity<>(JSON.toJSONString(params), headers);
		ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, request,
																		 JSONObject.class);
		if (response.getStatusCode() != HttpStatus.OK) {
			throw new ResponseStatusException(response.getStatusCode(), "请求出错");
		}
		return response.getBody();
	}



	/**
	 * @param url:    访问地址
	 * @param params: 请求参数，需要是一个Object类的对象，自己定义好成员属性
	 * @description 发送post请求，该重载方法用于已经装好的请求体，直接传入对应的对象类型即可，没有上面灵活，适用于和数据库相关的实体
	 */
	
	public <T> JSONObject post(String url, T params) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<T> request = new HttpEntity<>(params, headers);
		ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, request,
		 JSONObject.class);
		if (response.getStatusCode() != HttpStatus.OK) {
			throw new ResponseStatusException(response.getStatusCode(), "请求出错");
		}
		return response.getBody();
	}
}
