package com.example.demo;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskProducer {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TaskProducer.class);
    private static final String TASKS_PROGRESS_QUEUE = "tasksProgress";
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TaskProducer(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(TaskMessage message) {
        log.info("Sending message: {}", message.toString());
        rabbitTemplate.convertAndSend(TASKS_PROGRESS_QUEUE, message);
    }
}
