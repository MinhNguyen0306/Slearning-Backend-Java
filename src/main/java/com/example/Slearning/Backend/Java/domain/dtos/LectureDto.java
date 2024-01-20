package com.example.Slearning.Backend.Java.domain.dtos;

import com.example.Slearning.Backend.Java.domain.entities.LectureFileAttach;
import com.example.Slearning.Backend.Java.domain.entities.VideoStorage;
import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureDto {
    private UUID id;
    private String title;
    private String description;
    private VideoStorage videoStorage;
    private Integer position;
    private List<LectureFileAttach> lectureFileAttaches;
    private boolean isPreviewed = false;
    private boolean isLast;
    private PublishStatus publishStatus;
}
