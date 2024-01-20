package com.example.Slearning.Backend.Java.domain.dtos;

import com.example.Slearning.Backend.Java.domain.entities.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelDto {
    private UUID id;
    private String title;
    private List<Course> courses;
}
