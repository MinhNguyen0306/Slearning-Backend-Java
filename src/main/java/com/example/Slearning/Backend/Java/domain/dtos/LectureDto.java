package com.example.Slearning.Backend.Java.domain.dtos;

import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureDto {

    private String title;
    private String description;
    private Blob video;
    private Integer position;
    private byte[] fileAttach;
    private boolean isPreviewed = false;
    private boolean isLast;
    private PublishStatus publishStatus;
}
