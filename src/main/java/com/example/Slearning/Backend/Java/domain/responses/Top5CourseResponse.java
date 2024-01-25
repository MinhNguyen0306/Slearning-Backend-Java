package com.example.Slearning.Backend.Java.domain.responses;

import com.example.Slearning.Backend.Java.domain.entities.Course;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Top5CourseResponse {
    private List<Course> courses = new ArrayList<>();
    private List<Integer> turnoverOfCourses = new ArrayList<>();
}
