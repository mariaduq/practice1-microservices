package com.example.demo.server;

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

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TaskController.class);

    private static final String NEW_TASK_QUEUE = "newTasks";

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private TasksManager tasksManager;

    @Autowired
    private WebSocketSessionManager sessionManager;

    public TaskController(TasksManager tasksManager, final RabbitTemplate rabbitTemplate) {
        this.tasksManager = tasksManager;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public ResponseEntity<TaskServer> createTask(@RequestBody TaskServer task) throws Exception {

        TaskServer createdTask = TaskServer.builder()
                                .text(task.getText())
                                .id(tasksManager.getTaskId())
                                .build();

        tasksManager.saveTask(createdTask);

        WebSocketSession webSocketSession = sessionManager.getSession(TaskWebSocketHandler.userId);

        if(webSocketSession != null) {
            log.info("User ID: {}", webSocketSession.getId());
            webSocketSession.sendMessage(new TextMessage("Task ID: " + createdTask.getId()));
        }
        
        log.info("Task ID: {}", createdTask.getId());

        log.info("Sending message: {}", createdTask.toString());
        rabbitTemplate.convertAndSend(NEW_TASK_QUEUE, createdTask);

        return ResponseEntity.ok(createdTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable int id) {
        TaskServer task = tasksManager.getTaskById(id);

        if(task != null){
            if (task.getId() == TaskListener.lastMessage.getId()) {
                return ResponseEntity.ok(TaskListener.lastMessage);
            }
            else {
                return ResponseEntity.ok(task);
            }
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}
