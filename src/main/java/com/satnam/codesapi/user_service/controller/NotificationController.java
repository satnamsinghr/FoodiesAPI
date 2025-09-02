package com.satnam.codesapi.user_service.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    @MessageMapping("/order-status")
    @SendTo("/topic/orders")
    public String sendUpdate(String message) {
        return message;
    }
}
