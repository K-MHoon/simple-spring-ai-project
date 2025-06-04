package com.kmhoon.imagetextgen.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ImageTextGenService {

    private final ChatModel chatModel;

    @Value("classpath:/system.message")
    private Resource defaultSystemMessage;

    public String analyzeImage(MultipartFile imageFile, String message) {
        String contentType = imageFile.getContentType();
        if(!MimeTypeUtils.IMAGE_PNG_VALUE.equals(contentType) &&
        !MimeTypeUtils.IMAGE_JPEG_VALUE.equals(contentType)) {
            throw new IllegalArgumentException("지원되지 않는 이미지 형식입니다.");
        }

        try {
            var media = new Media(MimeType.valueOf(contentType), imageFile.getResource());
            var userMessage = new UserMessage(message, media);
            var systemMessage = new SystemMessage(defaultSystemMessage);

            return chatModel.call(userMessage, systemMessage);
        } catch (Exception e) {
            throw new RuntimeException("이미지 처리 중 오류가 발생했습니다.", e);
        }

    }

    public List<String> searchYouTubeVideos(String query) {
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q=EBS " +
                query + "&order=relevance&key=";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());

        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONArray items = jsonResponse.getJSONArray("items");

        return IntStream.range(0, items.length())
                .mapToObj(i -> {
                    JSONObject item = items.getJSONObject(i);
                    return item.getJSONObject("id").getString("videoId");
                }).toList();
    }

    public String extractKeyYouTubeSearch(String analysisText) {
        if(!analysisText.contains("핵심 키워드:")) {
            return null;
        }
        return analysisText.substring(analysisText.indexOf("핵심 키워드:")).split(":")[1].trim();
    }
}
