package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.dtos.ChapterDto;
import com.example.Slearning.Backend.Java.domain.entities.Chapter;
import com.example.Slearning.Backend.Java.domain.entities.Course;
import com.example.Slearning.Backend.Java.domain.entities.Lecture;
import com.example.Slearning.Backend.Java.domain.mappers.ChapterMapper;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.repositories.ChapterRepository;
import com.example.Slearning.Backend.Java.repositories.CourseRepository;
import com.example.Slearning.Backend.Java.services.ChapterService;
import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private CourseRepository courseRepository;

    private final ChapterMapper chapterMapper;

    @Override
    public ChapterDto createChapter(UUID courseId, String title, String description) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId.toString()));

        boolean checkExisted = course.getChapters().stream()
                .anyMatch(chapter -> chapter.getTitle().equalsIgnoreCase(title));

        if(checkExisted) {
            throw new IllegalStateException("Chapter da ton tai");
        }

        Chapter chapter = new Chapter();
        chapter.setTitle(title);
        chapter.setDescription(description);
        chapter.setCompleted(false);
        chapter.setPublishStatus(PublishStatus.PUBLISHING);

        List<Chapter> existedChapters = course.getChapters();
        Collections.sort(existedChapters);

        if(existedChapters.size() > 0) {
            chapter.setPosition(existedChapters.get(existedChapters.size() - 1).getPosition() + 1);
        } else {
            chapter.setPosition(1);
        }

        course.addChapter(chapter);
        Course updated = this.courseRepository.save(course);
        chapter.setCourse(updated);
        Chapter stored = this.chapterRepository.save(chapter);
        return this.chapterMapper.chapterToDto(stored);
    }

    @Override
    public ChapterDto getChapterById(UUID chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "Id", chapterId));
        ChapterDto chapterDto = this.chapterMapper.chapterToDto(chapter);
        return chapterDto;
    }

    @Override
    public ChapterDto getChapterOfLecture(UUID lectureId) {
        Optional<Chapter> chapter = this.chapterRepository.getChapterOfLecture(lectureId);
        if(chapter.isPresent()) {
            return this.chapterMapper.chapterToDto(chapter.get());
        } else {
            throw new ResourceNotFoundException("Chapter", "Lecture", lectureId);
        }
    }

    @Override
    public ChapterDto updateChapter(ChapterDto chapterDto, UUID chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "Id", chapterId));
        chapter.setTitle(chapterDto.getTitle());
        chapter.setDescription(chapterDto.getDescription());
        Chapter updatedChapter = chapterRepository.save(chapter);
        return this.chapterMapper.chapterToDto(updatedChapter);
    }

    @Override
    public boolean updateChapterPosition(UUID chapterId, UUID chapterDestinationID) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "Id", chapterId));
        Chapter chapterDestination = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "Id", chapterDestinationID));
        chapterDestination.setPosition(chapter.getPosition());
        chapter.setPosition(chapterDestination.getPosition());
        Chapter updatedChapter = chapterRepository.save(chapter);
        Chapter updatedChapterDestination = chapterRepository.save(chapterDestination);
        if(updatedChapter != null && updatedChapterDestination != null) {
            return true;
        }
        return false;
    }

    @Override
    public ChapterDto updatePublishStatus(PublishStatus publishStatus, UUID chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "Id", chapterId));
        chapter.setPublishStatus(publishStatus);
        Chapter updatedChapter = chapterRepository.save(chapter);
        return this.chapterMapper.chapterToDto(updatedChapter);
    }

    @Override
    public void deleteChapter(UUID chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "Id", chapterId));
        chapterRepository.delete(chapter);
    }
}
