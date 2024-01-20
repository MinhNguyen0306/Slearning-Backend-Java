package com.example.Slearning.Backend.Java.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Table(name = "topics")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE topics SET deleted = true where topic_id = ?")
@Data
@AllArgsConstructor @NoArgsConstructor
public class Topic extends BaseEntity {

    @Column(name = "topic_title")
    private String title;

    private boolean isLock = false;

    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courses;

    public void addCourse(Course course) {
        this.courses.add(course);
    }
}
