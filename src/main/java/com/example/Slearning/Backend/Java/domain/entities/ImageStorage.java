package com.example.Slearning.Backend.Java.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToOne(mappedBy = "image")
    @JsonIgnore
    private Course course;

    @OneToOne(mappedBy = "avatar")
    @JsonIgnore
    private User user;
}
