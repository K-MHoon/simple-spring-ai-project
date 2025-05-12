package com.kmhoon.openai.controller;

import com.kmhoon.openai.entity.Answer;
import com.kmhoon.openai.entity.Movie;
import com.kmhoon.openai.service.ChatService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

// 클라이언트의 요청을 받아서 JSON 형식으로 응답하는 컨트롤러
@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // LLM(gpt-4o)와 통신할 수 있는 객체: ChatClient

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        return chatService.chat(message);
    }

    @GetMapping("/chatMessage")
    public String chatMessage(@RequestParam("message") String message) {
        return chatService.chatMessage(message);
    }

    @GetMapping("/chatPlace")
    public String chatPlace(String subject, String tone, String message) {
        return chatService.chatPlace(subject, tone, message);
    }

    @GetMapping("/chatJson")
    public ChatResponse chatJson(String message) {
        return chatService.chatJson(message);
    }

    @GetMapping("/chatObject")
    public Answer chatObject(String message) {
        return chatService.chatObject(message);
    }

    @GetMapping("/recipe")
    public Answer recipe(String foodName, String question) {
        return chatService.recipe(foodName, question);
    }

    @GetMapping("/chatList")
    public List<String> chatlist(String message) {
        return chatService.chatList(message);
    }

    @GetMapping("/chatMap")
    public Map<String, String> chatMap(String message) {
        return chatService.chatMap(message);
    }

    @GetMapping("/chatMovie")
    public List<Movie> chatMovie(String directorName) {
        return chatService.chatMovie(directorName);
    }
}
