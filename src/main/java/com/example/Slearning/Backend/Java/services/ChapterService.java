package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.dtos.ChapterDto;
import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;

import java.util.UUID;

public interface ChapterService {
    ChapterDto createChapter(UUID courseId, ChapterDto chapterDto);
    ChapterDto getChapterById(UUID chapterId);
    ChapterDto updateChapter(ChapterDto chapterDto, UUID chapterId);
    boolean updateChapterPosition(UUID chapterId, UUID chapterDestinationID);
    ChapterDto updatePublishStatus(PublishStatus publishStatus, UUID chapterId);
    void deleteChapter(UUID chapterId);
}
