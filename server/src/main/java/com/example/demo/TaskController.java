package com.example.demo;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TaskController.class);

    private static final String NEW_TASK_QUEUE = "newTasks";

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private TasksManager tasksManager;

    //private Integer userId = 0;

    @Autowired
    private WebSocketSessionManager sessionManager;

    public TaskController(TasksManager tasksManager, final RabbitTemplate rabbitTemplate) {
        this.tasksManager = tasksManager;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, HttpSession httpSession) throws Exception {

        Task createdTask = Task.builder()
                                .text(task.getText())
                                .id(tasksManager.getTaskId())
                                .build();

        tasksManager.saveTask(createdTask);

        WebSocketSession webSocketSession = sessionManager.getSession(TaskWebSocketHandler.userId);

        if(webSocketSession != null) {
            log.info("User ID: {}", webSocketSession.getId());
            webSocketSession.sendMessage(new TextMessage("ID de la tarea creada: " + createdTask.getId()));
        }
        
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

    private String getHost() {
        String host = "localhost";
        int port = 8080;
        return host + ":" + port;
    }
}
