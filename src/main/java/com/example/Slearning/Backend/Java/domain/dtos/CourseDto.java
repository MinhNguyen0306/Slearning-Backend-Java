package com.example.Slearning.Backend.Java.domain.dtos;

import com.example.Slearning.Backend.Java.domain.entities.*;
import com.example.Slearning.Backend.Java.utils.enums.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Blob;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    private UUID id;
    @NotEmpty(message = "Title not empty")
    @NotBlank(message = "Title not blank")
    private String title;

    @NotNull(message = "Course must be have a display image")
    private ImageStorage image;

    @NotEmpty(message = "Description not empty")
    @NotBlank(message = "Description not blank")
    private String description;

    @NotEmpty(message = "Introduce not empty")
    @NotBlank(message = "Introduce not blank")
    private String introduce;

    @NotEmpty(message = "Requirement not empty")
    @NotBlank(message = "Requirement not blank")
    private String requirement;

    @NotEmpty(message = "Achievement not empty")
    @NotBlank(message = "Achievement not blank")
    private String achievement;

    private Double price;
    private CourseStatus status;
    private boolean isAdvertising = false;
    private boolean isComplete = false;
    private List<Chapter> chapters;
    private Level level;
    private Topic topic;
//    private Set<CourseRating> ratings;
    private User user;
}
