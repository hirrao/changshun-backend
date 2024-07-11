package com.pig4cloud.pig.patient.controller;

import com.alibaba.fastjson.JSONObject;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.plugin.websocket.config.WebSocketMessageSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h3>patient</h3>
 *
 * @author HuangJiayu
 * @description <p>基于websocket实现实时对话</p>
 * @date 2024-07-11 15:38
 **/
@RequestMapping("/msg")
@RestController
public class WebsocketController {
	
	@Inner(value = false)
	@PostMapping("send")
	public R sendMsg(@RequestBody JSONObject jsonObject) {
		String msg = jsonObject.getString("msg");
		String userName = jsonObject.getString("userName");
		WebSocketMessageSender.send(userName, msg);
		return R.ok();
	}
}
