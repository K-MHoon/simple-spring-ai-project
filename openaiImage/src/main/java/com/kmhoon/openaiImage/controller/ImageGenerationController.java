package com.kmhoon.openaiImage.controller;

import com.kmhoon.openaiImage.entity.ImageRequestDTO;
import com.kmhoon.openaiImage.service.ImageService;
import org.springframework.ai.image.ImageResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ImageGenerationController {

    private final ImageService imageService;

    public ImageGenerationController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/image")
    public List<String> image(@RequestBody ImageRequestDTO request) {
        ImageResponse imageResponse = imageService.getImageGen(request);
        return imageResponse.getResults()
                .stream()
                .map(result -> result.getOutput().getUrl())
                .toList();
    }
}
