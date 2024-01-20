package com.example.Slearning.Backend.Java.domain.dtos;

import com.example.Slearning.Backend.Java.domain.entities.Course;
import com.example.Slearning.Backend.Java.domain.entities.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDto {
    private UUID id;
    private String title;
    private boolean isLock;
    private SubCategory subCategory;
    private List<Course> courses;
}
