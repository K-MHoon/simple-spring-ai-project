package com.kmhoon.openaiImage.service;

import com.kmhoon.openaiImage.entity.ImageRequestDTO;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    private final OpenAiImageModel openAiImageModel;

    public ImageService(OpenAiImageModel openAiImageModel) {
        this.openAiImageModel = openAiImageModel;
    }

    public ImageResponse getImageGen(ImageRequestDTO request) {
        return openAiImageModel.call(new ImagePrompt(request.getMessage(),
                OpenAiImageOptions.builder()
                        .withModel(request.getModel())
                        .withQuality("hd") // quality 옵션은 dall-e-3에서만 지원한다.
                        .withN(request.getN())
                        .withHeight(1024)
                        .withWidth(1024)
                        .build()));
    }
}
