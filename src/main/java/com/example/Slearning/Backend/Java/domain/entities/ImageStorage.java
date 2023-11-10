package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "image_storages")
@Data
@NoArgsConstructor @AllArgsConstructor
public class ImageStorage extends BaseEntity {

    @Column(name = "image_name")
    private String name;

    @Column(name = "image_size")
    private long size;

    @Column(name = "image_url")
    private String url;

    @Column(name = "image_type")
    private String extension;

    @OneToMany(mappedBy = "image", fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();

    @ManyToMany(mappedBy = "imageStorages")
    @Column(name = "user_image_storages")
    private List<User> users;

    @OneToOne
    @MapsId
    private User user;

    public void addCourse(Course course) {
        this.courses.add(course);
    }
}
