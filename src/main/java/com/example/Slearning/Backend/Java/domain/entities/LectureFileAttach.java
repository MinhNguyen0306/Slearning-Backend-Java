package com.example.Slearning.Backend.Java.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "lecture_file_attach")
@Data
@NoArgsConstructor @AllArgsConstructor
public class LectureFileAttach extends BaseEntity {

    private String fileName;

    private String fileUrl;

    @Column(name = "file_extension")
    private String fileType;

    private long fileSize;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    @JsonIgnore
    private Lecture lecture;
}
