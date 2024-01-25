package com.example.Slearning.Backend.Java.domain.responses;

import com.example.Slearning.Backend.Java.domain.entities.Topic;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Top5TopicResponse {
    private List<String> topics = new ArrayList<>();
    private List<Integer> numberCourse = new ArrayList<>();
}
