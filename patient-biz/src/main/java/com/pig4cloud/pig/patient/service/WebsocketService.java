package com.pig4cloud.pig.patient.service;

import com.pig4cloud.pig.patient.entity.PatientBaseEntity;
import com.pig4cloud.plugin.websocket.config.WebSocketMessageSender;
import com.pig4cloud.plugin.websocket.handler.PlanTextMessageHandler;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	private PatientBaseService patientBaseService;
	
	// 存储 WebSocket 会话的映射
	private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
	
	
	/**
	 * 普通文本消息处理，客制化自己的对于接收消息的时的操作
	 *
	 * @param session 当前接收消息的session
	 * @param message 文本消息
	 */
	@Override
	public void handle(WebSocketSession session, String message) {
		// 业务逻辑处理
		log.info("session_id :{} ,接收到的message: {}", session.getId(), message);
		//sessions.put(session.getAttributes().get("userName").toString(), session);
	}
	
	public boolean sendMsg(String userName, String message) {
		try {
			WebSocketMessageSender.send(userName, message);
			return true;
		} catch (Exception e) {
			log.error("发送消息失败，用户没有建立连接");
			return false;
		}
	}
	
	public boolean sendMsg(Long patientUid, String message) {
		//	查表根据患者id获取患者的userName
		PatientBaseEntity tmp = patientBaseService.getById(patientUid);
		if (tmp == null) {
			log.error("患者不存在");
			return false;
		}
		String userName = tmp.getUsername();
		return sendMsg(userName, message);
	}
	
	// 新增方法来检查会话是否存在
	public boolean sessionExists(String userName) {
		return sessions.containsKey(userName);
	}
}
