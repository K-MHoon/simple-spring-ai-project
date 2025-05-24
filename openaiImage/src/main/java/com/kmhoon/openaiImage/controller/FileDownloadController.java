package com.kmhoon.openaiImage.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class FileDownloadController {

    private final RestTemplate restTemplate;

    public FileDownloadController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/download-file")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String url) {
        try {
            URI uri = new URI(url);

            ResponseEntity<byte[]> response = restTemplate.getForEntity(uri, byte[].class);

            String fileName = extractFileName(url);

            HttpHeaders downloadHeaders = new HttpHeaders();
            // APPLICATION_OCTET_STREAM는 파일 다운로드
            downloadHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            downloadHeaders.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());

            return new ResponseEntity<>(response.getBody(), downloadHeaders, HttpStatus.OK);

        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body(("Failed to download file: " + e.getMessage()).getBytes());
        }

    }

    private String extractFileName(String url) {
        // url 구조가 마지막에 xxx/image.png?param 형식으로 고정되어 정해져있으므로 추출해야 한다.
        String path = URI.create(url).getPath();
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
