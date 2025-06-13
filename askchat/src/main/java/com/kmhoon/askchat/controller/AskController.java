package com.kmhoon.askchat.controller;

import com.kmhoon.askchat.service.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AskController {

    private final ChatService chatService;


    public AskController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/ask")
    public String getResponse(String message) {
        return chatService.getResponse(message); // 응답(text) --> await
    }

    @GetMapping("/ask-ai")
    public String getResponseOptions(String message) {
        return chatService.getResponseOptions(message); // 응답(text) --> await
    }
}
