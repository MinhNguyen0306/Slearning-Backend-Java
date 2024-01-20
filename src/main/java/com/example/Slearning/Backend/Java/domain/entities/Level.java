package com.example.Slearning.Backend.Java.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Table(name = "levels")
@Data
@NoArgsConstructor
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private Integer id;

    @Column(name = "level_title", nullable = false, unique = true)
    private String title;

    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courses;

    public void addCourse(Course course) {
        this.courses.add(course);
    }
}
