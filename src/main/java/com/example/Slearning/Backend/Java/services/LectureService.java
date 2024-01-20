package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.dtos.LectureDto;
import com.example.Slearning.Backend.Java.domain.entities.Lecture;
import com.example.Slearning.Backend.Java.domain.entities.LectureFileAttach;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface LectureService {
    LectureDto createLecture(UUID chapterId, String title, String description);

    LectureDto getLectureById(UUID lectureId, PublishStatus publishStatus);

    LectureDto getNextLecture(UUID chapterId, Integer prevPosition);

    boolean updatePreviewed(UUID lectureId);
    PublishStatus updatePublishing(UUID lectureId);

    LectureDto updateLecture(LectureDto lectureDto, UUID lectureId);

    boolean updateLecturePosition(UUID lectureId, UUID lectureDestinationId);

    LectureDto updatePublishStatus(UUID lectureId);

    LectureDto updatePreviewStatus(UUID lectureId);

    LectureDto uploadLectureVideo(UUID lectureId, MultipartFile videoFile, Double videoDuration);

    List<LectureFileAttach> getFilesAttachOfLecture(UUID lectureId);

    boolean uploadLectureFileAttach(UUID lectureId, MultipartFile[] fileAttach);

    void deleteLectureFileAttach(UUID fileId);

    void deleteLecture(UUID lectureId);
}
