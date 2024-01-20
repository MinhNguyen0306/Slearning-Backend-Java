package com.example.Slearning.Backend.Java.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "video_poster")
    private String posterUrl;

    @Column(name = "video_size")
    private long size;

    @Column(name = "video_duration")
    private Double duration;

    @Column(name = "video_url")
    private String url;

    @Column(name = "video_type")
    private String extension;

    @OneToOne(mappedBy = "videoStorage")
    @JsonIgnore
    private Lecture lecture;
}
