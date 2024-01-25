package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrackingCodingEx {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String prevLectureId;

    private String nextLectureId;

    private boolean completed = false;

    private String userCoding;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private CodingExercise codingExercise;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
