package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

@Service
public class TasksManager {

    private Map <Integer, Task> tasks;
    private Integer taskId;

    public TasksManager() {
        tasks = new HashMap<>();
        taskId = 0;
    }

    public synchronized Integer getTaskId() {
        return taskId++;
    }

    public synchronized void saveTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public Task getTaskById(Integer id) {
        if(tasks.containsKey(id)) {
            System.out.println(tasks.get(id));
            return tasks.get(id);
            
        }
        else throw new NoSuchElementException("Element with id: " + id + " not found in map.");
    }

}
