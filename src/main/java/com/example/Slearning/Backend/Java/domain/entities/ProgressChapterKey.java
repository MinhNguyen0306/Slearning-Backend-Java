package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
public class ProgressChapterKey implements Serializable {

    @Column(name = "progress_id")
    UUID progressId;

    @Column(name = "chapter_id")
    UUID chapterId;
}
