package com.pig4cloud.pig.patient.service;

import com.pig4cloud.plugin.websocket.handler.PlanTextMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

/**
 * <h3>patient</h3>
 *
 * @author HuangJiayu
 * @description <p>Websocket服务类</p>
 * @date 2024-07-11 15:44
 **/
@Service
@Slf4j
public class WebsocketService implements PlanTextMessageHandler {
	
	/**
	 * 普通文本消息处理，客制化自己的对于接收消息的时的操作
	 *
	 * @param session 当前接收消息的session
	 * @param message 文本消息
	 */
	@Override
	public void handle(WebSocketSession session, String message) {
		// TODO: 业务逻辑处理
		log.info("session_id :{} ,接收到的message: {}", session.getId(), message);
	}
}
