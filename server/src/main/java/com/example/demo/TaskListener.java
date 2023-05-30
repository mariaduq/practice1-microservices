package com.example.demo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TaskListener {

    Logger logger = LoggerFactory.getLogger(TaskListener.class);

    @RabbitListener(queues = "tasksProgress", ackMode = "AUTO")
    public void received(TaskMessage message) throws InterruptedException {

        System.out.println("Message: "+message);
        logger.info("Received message as generic: {}", message.toString());
    } 
}

