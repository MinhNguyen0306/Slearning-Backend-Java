package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.dtos.LectureDto;
import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface LectureService {
    LectureDto createLecture(UUID chapterId, LectureDto lectureDto);
    LectureDto getLectureById(UUID lectureId);
    LectureDto updateLecture(LectureDto lectureDto, UUID lectureId);
    boolean updateLecturePosition(UUID lectureId, UUID lectureDestinationId);
    LectureDto updatePublishStatus(UUID lectureId);
    LectureDto updatePreviewStatus(UUID lectureId);
    LectureDto uploadLectureVideo(MultipartFile videoFile);
    LectureDto uploadLectureFileAttach(byte[] fileAttach);
    void deleteLecture(UUID lectureId);
}
