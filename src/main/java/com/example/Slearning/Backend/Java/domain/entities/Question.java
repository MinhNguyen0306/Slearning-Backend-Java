package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE questions SET deleted = true where question_id = ?")
@Data
@NoArgsConstructor @AllArgsConstructor
public class Question extends BaseEntity {
    @Column(name = "question_title", nullable = false)
    @Min(value = 5, message = "Min length is 5")
    private String question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();
}
