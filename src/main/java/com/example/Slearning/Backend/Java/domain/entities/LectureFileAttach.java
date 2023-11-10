package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "lecture_file_attach")
@Data
@NoArgsConstructor @AllArgsConstructor
public class LectureFileAttach {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "lecture_file_attach_id", nullable = false)
    private UUID id;

    @Column(name = "lecture_file_attach")
    @Lob
    private byte[] fileAttach;

    @Column(name = "file_extension")
    private String fileType;

    private Double fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;
}
