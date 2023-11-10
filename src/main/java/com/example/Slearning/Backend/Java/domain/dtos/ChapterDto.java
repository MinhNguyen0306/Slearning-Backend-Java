package com.example.Slearning.Backend.Java.domain.dtos;

import com.example.Slearning.Backend.Java.domain.entities.Course;
import com.example.Slearning.Backend.Java.domain.entities.Lecture;
import com.example.Slearning.Backend.Java.domain.entities.Question;
import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDto {

    private String description;
    private String title;
    private Integer position;
    private PublishStatus publishStatus;
    private boolean isLast;
    private boolean isCompleted;
    private Course course;
    private List<Lecture> lectures = new ArrayList<>();
    private List<Question> questions = new ArrayList<>();
}
