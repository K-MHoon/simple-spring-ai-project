package com.kmhoon.openai.service;

import com.kmhoon.openai.entity.Answer;
import com.kmhoon.openai.entity.Movie;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Service
public class ChatService {

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    private final ChatClient chatClient;


    public String chat(String message) {
        return chatClient.prompt()
                .user(message) // 사용자 메시지
                .call() // 호출
                .content(); // ChatResponse -> 요청정보를 받는 부분(문자열)
    }

    public String chatMessage(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .chatResponse() // ChatResponse
                .getResult()
                .getOutput()
                .getText();
    }

    public String chatPlace(String subject, String tone, String message) {
        return chatClient.prompt()
                .user(message)
                .system(sp -> sp
                        .param("subject", subject)
                        .param("tone", tone))
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();
    }

    public ChatResponse chatJson(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .chatResponse(); // ChatResponse(?) --> JSON
    }

    public Answer chatObject(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .entity(Answer.class);
    }

    private final String recipeTemplate = """
            Answer for {foodName} for {question} ?
            """;

    public Answer recipe(String foodName, String question) {
        return chatClient.prompt()
                .user(userSpec -> userSpec.text(recipeTemplate)
                        .param("foodName", foodName)
                        .param("question", question))
                .call()
                .entity(Answer.class);
    }

    public List<String> chatList(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .entity(new ListOutputConverter(new DefaultConversionService()));
    }

    public Map<String, String> chatMap(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .entity(new ParameterizedTypeReference<Map<String, String>>() {});
    }

    public List<Movie> chatMovie(String directorName) {
        String template = """
                "Generate a list of movies directed by {directorName}. If the director is unknown, return null.
                한국 영화는 한글로 표기해줘.
                Each movie should include a title and release year. {format}"
                """;

        List<Movie> movieList = chatClient.prompt()
                .user(spec -> spec.text(template)
                        .param("directorName", directorName)
                        .param("format", "json")
                )
                .call()
                .entity(new ParameterizedTypeReference<List<Movie>>() {});

        return movieList;
    }

    public String getResponse(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    public void startChat() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your message:");
        while(true) {
            String message = scanner.nextLine();
            if(message.equals("exit")) {
                System.out.println("Exiting chat");
                break;
            }
            String response = getResponse(message);
            System.out.println("Bot: " + response);
        }
        scanner.close();
    }
}
