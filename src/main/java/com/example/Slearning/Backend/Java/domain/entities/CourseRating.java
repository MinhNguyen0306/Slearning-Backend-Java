package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_ratings")
@Data
@NoArgsConstructor
public class CourseRating {
    @EmbeddedId
    private CourseRatingKey id;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_rating_course")
    private Integer rating;
}
