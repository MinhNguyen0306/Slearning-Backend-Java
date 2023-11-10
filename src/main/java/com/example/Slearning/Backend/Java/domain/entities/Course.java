package com.example.Slearning.Backend.Java.domain.entities;

import com.example.Slearning.Backend.Java.utils.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "courses")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE courses SET deleted = true where id = ?")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course extends BaseEntity {
    @Column(name = "course_title")
    private String title;

    @Column(name = "course_description")
    private String description;

    @Column(name = "course_introduce")
    private String introduce;

    @Column(name = "course_requirement")
    private String requirement;

    @Column(name = "course_achievement")
    private String achievement;

    @Column(name = "course_price")
    private Double price;

    @Column(name = "course_status")
    private CourseStatus status;

    private boolean isAdvertising = false;

    private boolean isComplete = false;

    @Column(name = "course_image")
    @ManyToOne
    @JoinColumn(name = "image_id")
    private ImageStorage image;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "level_id")
    private Level level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToMany(mappedBy = "course")
    private Set<CourseRating> ratings = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Enroll> enrolls;

    public void addChapter(Chapter chapter) {
        this.chapters.add(chapter);
    }
}
