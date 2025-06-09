package com.kmhoon.imagetextgen.controller;

import com.kmhoon.imagetextgen.entity.ImageAnalysisVO;
import com.kmhoon.imagetextgen.service.ImageTextGenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/image-text")
@RequiredArgsConstructor
public class ImageTextGenController {

    private final ImageTextGenService imageTextGenService;

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/analyze")
    public ResponseEntity<ImageAnalysisVO> getMultimodalResponse(@RequestParam("image") MultipartFile imageFile,
                                   @RequestParam(defaultValue = "이 이미지에 무엇이 있나요?") String message) throws IOException {
        File uploadDirectory = new File(uploadPath);
        if(!uploadDirectory.exists()){
            uploadDirectory.mkdirs();
        }

        String fileName = imageFile.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, fileName);
        Files.write(filePath, imageFile.getBytes());

        String analysisText = imageTextGenService.analyzeImage(imageFile, message);

        String searchKeyword = imageTextGenService.extractKeyYouTubeSearch(analysisText);
        List<String> youtubeUrls = imageTextGenService.searchYouTubeVideos(searchKeyword);

        String imageUrl = "/uploads/" + fileName;

        ImageAnalysisVO response = new ImageAnalysisVO(imageUrl, analysisText, youtubeUrls);
        return ResponseEntity.ok(response);
    }
}
