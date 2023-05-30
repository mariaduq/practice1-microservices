package com.example.demo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TaskListener {

    Logger logger = LoggerFactory.getLogger(TaskListener.class);

    private SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = "tasksProgress", ackMode = "AUTO")
    public void received(TaskMessage message) throws InterruptedException, IOException {

        System.out.println("Message: "+message);
        logger.info("Received message as generic: {}", message.toString());

        //messagingTemplate.convertAndSendToUser(null, null, message, null, null);
    } 
}
