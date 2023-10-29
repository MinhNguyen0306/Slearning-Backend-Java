package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
public class CourseRatingKey implements Serializable {
    @Column(name = "course_id")
    UUID courseID;

    @Column(name = "user_id")
    UUID userID;
}
