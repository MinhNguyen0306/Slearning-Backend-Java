package com.example.Slearning.Backend.Java.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice implements Serializable {
    private String title;
    private String topic;
    private String content;
    private String imageUrl;

    private Map<String, String> data;
    private List<String> deviceTokens;
}
