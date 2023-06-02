package com.example.demo.worker;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TaskProducer {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TaskProducer.class);
    private static final String TASKS_PROGRESS_QUEUE = "tasksProgress";

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    public TaskProducer(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(TaskMessage taskMessage) throws Exception {
        log.info("Sending message: {}", taskMessage.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonTaskMessage = objectMapper.writeValueAsString(taskMessage);

        rabbitTemplate.convertAndSend(TASKS_PROGRESS_QUEUE, jsonTaskMessage, message -> {
            message.getMessageProperties().setContentType("application/json");
            return message;
        });

    }
}
