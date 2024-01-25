package com.example.Slearning.Backend.Java.domain.entities;

import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodingExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String solution;

    private String evaluation;

    private String result;

    private Integer languageId;

    private String instruction;

    private String solutionExplanation;

    private String hint;

    private String codeStarter;

    @Enumerated(EnumType.STRING)
    private PublishStatus publishStatus;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @OneToMany(mappedBy = "codingExercise", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TrackingCodingEx> trackingCodingExes;
}
