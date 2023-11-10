package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "video_storages")
@Data
@NoArgsConstructor @AllArgsConstructor
public class VideoStorage extends BaseEntity {

    @Column(name = "video_name")
    private String name;

    @Column(name = "video_size")
    private long size;

    @Column(name = "video_url")
    private String url;

    @Column(name = "video_type")
    private String extension;

    @OneToOne
    @MapsId
    private Lecture lecture;
}
