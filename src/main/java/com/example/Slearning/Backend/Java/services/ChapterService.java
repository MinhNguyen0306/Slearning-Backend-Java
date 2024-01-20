package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.dtos.ChapterDto;
import com.example.Slearning.Backend.Java.domain.entities.Chapter;
import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;

import java.util.UUID;

public interface ChapterService {
    ChapterDto createChapter(UUID courseId, String title, String description);
    ChapterDto getChapterById(UUID chapterId);

    ChapterDto getChapterOfLecture(UUID lectureId);

    ChapterDto updateChapter(ChapterDto chapterDto, UUID chapterId);
    boolean updateChapterPosition(UUID chapterId, UUID chapterDestinationID);
    ChapterDto updatePublishStatus(PublishStatus publishStatus, UUID chapterId);
    void deleteChapter(UUID chapterId);
}
