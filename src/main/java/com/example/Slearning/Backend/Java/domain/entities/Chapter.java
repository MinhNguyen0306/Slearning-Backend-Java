package com.example.Slearning.Backend.Java.domain.entities;

import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "chapters")
@Data
@NoArgsConstructor @AllArgsConstructor
public class Chapter extends BaseEntity implements Comparable<Chapter> {
    @Column(name = "chapter_description")
    private String description;

    @Column(name = "chapter_title", nullable = false)
    private String title;

    @Column(name = "chapter_position")
    private Integer position;

    @Column(name = "chapter_publish_status")
    private PublishStatus publishStatus;

    @Column(name = "is_last_chapter")
    private boolean isLast;

    @Column(name = "is_completed_chapter")
    private boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private Course course;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lecture> lectures;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CodingExercise> codingExercises;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    public void addLecture(Lecture lecture) {
        this.lectures.add(lecture);
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    @Override
    public int compareTo(Chapter chapter) {
        if(this.getPosition() > chapter.getPosition()) {
            return 1;
        } else if(this.getPosition() < chapter.getPosition()) {
            return -1;
        } else {
            return 0;
        }
    }
}
