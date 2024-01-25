package com.example.Slearning.Backend.Java.domain.responses;

import com.example.Slearning.Backend.Java.domain.entities.Course;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Top5RateResponse {
    private List<String> courses = new ArrayList<>();
    private List<Integer> rateCourse = new ArrayList<>();
}
