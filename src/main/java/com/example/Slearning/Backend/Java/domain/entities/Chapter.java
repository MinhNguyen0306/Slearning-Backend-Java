package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chapters")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE chapter SET deleted = true where id = ?")
@Data
@NoArgsConstructor @AllArgsConstructor
public class Chapter extends BaseEntity {
    @Column(name = "chapter_description")
    private String description;

    @Column(name = "chapter_title", nullable = false)
    private String title;

    @Column(name = "visible_position")
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Lecture> lectures = new ArrayList<>();

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();
}
