package com.kmhoon.imagetextgen.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ImageAnalysisVO {

    private String imageUrl;
    private String analysisText;
    private List<String> youtubeUrls = new ArrayList<>();
}
