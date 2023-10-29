package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "answers")
@Data
@NoArgsConstructor
public class Answer extends BaseEntity {

    @Column(name = "answer_content", nullable = false)
    @Min(value = 1, message = "At least 1 character")
    @NotEmpty(message = "Answer not empty")
    private String answer;

    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
