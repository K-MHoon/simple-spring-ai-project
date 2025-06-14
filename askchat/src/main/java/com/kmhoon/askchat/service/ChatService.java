package com.kmhoon.askchat.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    // ChatClient
    private final ChatModel chatModel; // LLM(OpenAI) - API Key

    public ChatService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String getResponse(String message) {
        return chatModel.call(message);
    }

    public String getResponseOptions(String message) {
        ChatResponse response = chatModel.call(new Prompt(
                message,
                OpenAiChatOptions.builder()
                        .withModel("gpt-4o")
                        .withTemperature(0.4)
                        .build()
        ));
        return response.getResult().getOutput().getContent();
    }
}
