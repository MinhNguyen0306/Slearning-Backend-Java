package com.example.Slearning.Backend.Java.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_ratings")
@Data
@NoArgsConstructor
public class CourseRating {

    @Column(name = "course_rating_id")
    @EmbeddedId
    private CourseRatingKey id;

    private Integer rating = 0;

    private String comment;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime createAt = LocalDateTime.now();

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime updateAt;
}
