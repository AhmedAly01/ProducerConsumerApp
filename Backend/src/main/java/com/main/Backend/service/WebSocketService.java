package com.main.Backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private static SimpMessagingTemplate messagingTemplate = null;

    @Autowired
    public WebSocketService(final SimpMessagingTemplate messagingTemplate){
        WebSocketService.messagingTemplate = messagingTemplate;
    }

    public static void notifyFrontend(final String message){
        messagingTemplate.convertAndSend("/topic/", message);
    }
}
