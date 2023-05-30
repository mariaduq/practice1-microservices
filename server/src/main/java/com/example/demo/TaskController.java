package com.example.demo;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TaskController.class);

    private static final String NEW_TASK_QUEUE = "newTasks";

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private TasksManager tasksManager;

    public TaskController(TasksManager tasksManager, final RabbitTemplate rabbitTemplate) {
        this.tasksManager = tasksManager;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {

        Task createdTask = Task.builder()
                                .text(task.getText())
                                .id(tasksManager.getTaskId())
                                .build();

        tasksManager.saveTask(createdTask);
        
        log.info("ID de la tarea creada: {}", createdTask.getId());

        log.info("Sending message: {}", createdTask.toString());
        rabbitTemplate.convertAndSend(NEW_TASK_QUEUE, createdTask);

        return ResponseEntity.ok(createdTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable int id) {
        Task task = tasksManager.getTaskById(id);

        if(task != null){
            return ResponseEntity.ok(task);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}
