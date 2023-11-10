package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.dtos.LectureDto;
import com.example.Slearning.Backend.Java.domain.entities.Chapter;
import com.example.Slearning.Backend.Java.domain.entities.Lecture;
import com.example.Slearning.Backend.Java.domain.mappers.LectureMapper;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.repositories.ChapterRepository;
import com.example.Slearning.Backend.Java.repositories.LectureRepository;
import com.example.Slearning.Backend.Java.services.LectureService;
import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(rollbackOn = {Exception.class, Throwable.class})
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    private final LectureMapper lectureMapper;

    @Override
    public LectureDto createLecture(UUID chapterId, LectureDto lectureDto) {
        Chapter chapter = this.chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "Id", chapterId));
        Lecture lecture = this.lectureMapper.dtoToLecture(lectureDto);
        chapter.addLecture(lecture);
        Chapter updatedChapter = this.chapterRepository.save(chapter);
        lecture.setChapter(updatedChapter);
        Lecture createdLecture = this.lectureRepository.save(lecture);
        return this.lectureMapper.lectureToDto(createdLecture);
    }

    @Override
    public LectureDto getLectureById(UUID lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId));
        LectureDto lectureDto = this.lectureMapper.lectureToDto(lecture);
        return lectureDto;
    }

    @Override
    public LectureDto updateLecture(LectureDto lectureDto, UUID lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId));
        lecture.setTitle(lectureDto.getTitle());
        lecture.setDescription(lectureDto.getDescription());
        lecture.setUpdateAt(LocalDateTime.now());
        Lecture updatedLecture = lectureRepository.save(lecture);
        return this.lectureMapper.lectureToDto(updatedLecture);
    }

    @Override
    public boolean updateLecturePosition(UUID lectureId, UUID lectureDestinationId) {
        Lecture lecture = this.lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId));
        Lecture lectureDestination = this.lectureRepository.findById(lectureDestinationId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureDestinationId));
        lecture.setPosition(lectureDestination.getPosition());
        lectureDestination.setPosition(lecture.getPosition());
        Lecture updatedLecture = this.lectureRepository.save(lecture);
        Lecture updatedLectureDestination = this.lectureRepository.save(lectureDestination);
        if(updatedLectureDestination != null && updatedLecture != null) {
            return true;
        }
        return false;
    }

    @Override
    public LectureDto updatePublishStatus(UUID lectureId) {
        Lecture lecture = this.lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId));
        if(lecture.getPublishStatus() == PublishStatus.PUBLISHING) {
            lecture.setPublishStatus(PublishStatus.UN_PUBLISHING);
        } else {
            lecture.setPublishStatus(PublishStatus.PUBLISHING);
        }
        Lecture updatedLecture = this.lectureRepository.save(lecture);
        return this.lectureMapper.lectureToDto(updatedLecture);
    }

    @Override
    public LectureDto updatePreviewStatus(UUID lectureId) {
        Lecture lecture = this.lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId));
        if(lecture.isPreviewed()) {
            lecture.setPreviewed(false);
        } else {
            lecture.setPreviewed(true);
        }
        Lecture updatedLecture = this.lectureRepository.save(lecture);
        return this.lectureMapper.lectureToDto(updatedLecture);
    }

    @Override
    public LectureDto uploadLectureVideo(MultipartFile videoFile) {
        return null;
    }

    @Override
    public LectureDto uploadLectureFileAttach(byte[] fileAttach) {
        return null;
    }

    @Override
    public void deleteLecture(UUID lectureId) {

    }
}
