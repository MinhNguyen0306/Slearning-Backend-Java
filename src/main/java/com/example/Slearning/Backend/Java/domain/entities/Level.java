package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Table(name = "levels")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE levels SET deleted = true where level_id = ?")
@Data
@NoArgsConstructor
public class Level extends BaseEntity {
    @Column(name = "level_title", nullable = false, unique = true)
    private String title;

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Course> courses;

    public void addCourse(Course course) {
        this.courses.add(course);
    }
}
