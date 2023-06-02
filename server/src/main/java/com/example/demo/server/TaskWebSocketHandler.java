package com.example.demo.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
public class TaskWebSocketHandler extends TextWebSocketHandler {

    Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

	public static Integer userId = 1;

	@Autowired
    private WebSocketSessionManager sessionManager;

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {

		logger.info("Message received: " + message.getPayload());
		session.sendMessage(new TextMessage(message.getPayload()));

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		logger.info("User disconnected "+session.getId());

		sessionManager.removeSession(userId);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info("User connected "+session.getId());
		session.sendMessage(new TextMessage("Hello user"));

		userId = userId + 1;

		session.getAttributes().put("userId", userId);

		Integer userId = (Integer) session.getAttributes().get("userId");
		sessionManager.addSession(userId, session);
	}
}
