package com.example.demo.server;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketSessionManager {
    
    private ConcurrentHashMap<Integer, WebSocketSession> activeSessions = new ConcurrentHashMap<>();

    public void addSession(Integer userId, WebSocketSession session) {
        activeSessions.put(userId, session);
    }

    public void removeSession(Integer userId) {
        activeSessions.remove(userId);
    }

    public WebSocketSession getSession(Integer userId) {
        return activeSessions.get(userId);
    }
}
