package com.example.demo.server;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TaskListener {

    Logger logger = LoggerFactory.getLogger(TaskListener.class);

    public static TaskMessageServer lastMessage;

    @Autowired
    private WebSocketSessionManager sessionManager;

    @RabbitListener(queues = "tasksProgress", ackMode = "AUTO")
    public void received(TaskMessageServer message) throws InterruptedException, IOException {

        lastMessage = message;
        
        System.out.println("Message: "+message);
        logger.info("Received message as generic: {}", message.toString());

        WebSocketSession webSocketSession = sessionManager.getSession(TaskWebSocketHandler.userId);

        if(webSocketSession != null) {
            webSocketSession.sendMessage(new TextMessage(message.toString()));
        }
    } 
}
