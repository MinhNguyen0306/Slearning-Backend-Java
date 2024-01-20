package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.dtos.LectureDto;
import com.example.Slearning.Backend.Java.domain.entities.Chapter;
import com.example.Slearning.Backend.Java.domain.entities.Lecture;
import com.example.Slearning.Backend.Java.domain.entities.LectureFileAttach;
import com.example.Slearning.Backend.Java.domain.entities.VideoStorage;
import com.example.Slearning.Backend.Java.domain.mappers.LectureMapper;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.exceptions.UploadFileException;
import com.example.Slearning.Backend.Java.repositories.ChapterRepository;
import com.example.Slearning.Backend.Java.repositories.LectureFileAttachRepository;
import com.example.Slearning.Backend.Java.repositories.LectureRepository;
import com.example.Slearning.Backend.Java.repositories.VideoStorageRepository;
import com.example.Slearning.Backend.Java.services.FileStorageService;
import com.example.Slearning.Backend.Java.services.LectureService;
import com.example.Slearning.Backend.Java.utils.FileUtils;
import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(rollbackOn = {Exception.class, Throwable.class})
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    @Value("${project.videos}")
    private String videoPath;

    @Value("${project.attachFiles}")
    private String attachFiles;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private VideoStorageRepository videoStorageRepository;

    @Autowired
    private LectureFileAttachRepository lectureFileAttachRepository;

    @Autowired
    private FileStorageService fileStorageService;

    private final LectureMapper lectureMapper;

    @Override
    public LectureDto createLecture(UUID chapterId, String title, String description) {
        Chapter chapter = this.chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "Id", chapterId.toString()));

        boolean checkExisted = chapter.getLectures().stream()
                .anyMatch(lecture -> lecture.getTitle().equalsIgnoreCase(title));

        if (checkExisted) {
            throw new IllegalStateException("Lecture da ton tai");
        }

        Lecture lecture = new Lecture();
        lecture.setTitle(title);
        lecture.setDescription(description);
        lecture.setPreviewed(false);
        lecture.setPublishStatus(PublishStatus.PUBLISHING);

        List<Lecture> existedLectures = chapter.getLectures();
        Collections.sort(existedLectures);

        if(existedLectures.size() > 0) {
            lecture.setPosition(existedLectures.get(existedLectures.size() - 1).getPosition() + 1);
        } else {
            lecture.setPosition(1);
        }

        chapter.addLecture(lecture);
        Chapter updatedChapter = this.chapterRepository.save(chapter);
        lecture.setChapter(updatedChapter);
        Lecture createdLecture = this.lectureRepository.save(lecture);
        return this.lectureMapper.lectureToDto(createdLecture);
    }

    @Override
    public LectureDto getLectureById(UUID lectureId, PublishStatus publishStatus) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId));
        if(publishStatus == null) {
            return this.lectureMapper.lectureToDto(lecture);
        } else {
            if(lecture.getPublishStatus().equals(PublishStatus.PUBLISHING) && publishStatus.equals(PublishStatus.PUBLISHING)) {
                return this.lectureMapper.lectureToDto(lecture);
            } else {
                return null;
            }
        }
    }

    @Override
    public LectureDto getNextLecture(UUID chapterId, Integer prevPosition) {
        Optional<Lecture> nextLecture = this.lectureRepository.getNextLecture(chapterId, prevPosition);
        if(nextLecture.isPresent()) {
            return this.lectureMapper.lectureToDto(nextLecture.get());
        } else {
            return null;
        }
    }

    @Override
    public boolean updatePreviewed(UUID lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId));
        if(lecture.isPreviewed()) {
            lecture.setPreviewed(false);
        } else {
            lecture.setPreviewed(true);
        }

        Lecture updated = this.lectureRepository.save(lecture);
        return updated.isPreviewed();
    }

    @Override
    public PublishStatus updatePublishing(UUID lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId));
        if(lecture.getPublishStatus().equals(PublishStatus.PUBLISHING)) {
            lecture.setPublishStatus(PublishStatus.UN_PUBLISHING);
        } else {
            lecture.setPublishStatus(PublishStatus.PUBLISHING);
        }

        Lecture updated = lectureRepository.save(lecture);
        return updated.getPublishStatus();
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
    public LectureDto uploadLectureVideo(UUID lectureId, MultipartFile videoFile, Double videoDuration) {
        Lecture lecture = this.lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId));
        VideoStorage prevVideo = lecture.getVideoStorage();
        try {
            String fileUrl = fileStorageService.uploadFile(videoPath, videoFile);
            String originalVideoName = videoFile.getOriginalFilename();
            String extension = FileUtils.getExtensionFile(fileUrl);
            VideoStorage videoStorage = new VideoStorage();
            videoStorage.setUrl(fileUrl);
            videoStorage.setSize(videoFile.getSize());
            videoStorage.setDuration(videoDuration);
            videoStorage.setExtension(extension);
            videoStorage.setLecture(lecture);
            videoStorage.setName(originalVideoName);
            VideoStorage storedVideo = this.videoStorageRepository.save(videoStorage);
            lecture.setVideoStorage(storedVideo);
            Lecture updatedLecture = this.lectureRepository.save(lecture);
            if(updatedLecture == null) {
                throw new RuntimeException("Upload thất bại");
            }

            if(prevVideo != null) {
                this.videoStorageRepository.delete(prevVideo);
            }

            return lectureMapper.lectureToDto(updatedLecture);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<LectureFileAttach> getFilesAttachOfLecture(UUID lectureId) {
        Lecture lecture = this.lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId));
        return lecture.getLectureFileAttaches();
    }

    @Override
    public boolean uploadLectureFileAttach(UUID lectureId, MultipartFile[] fileAttach) {
        Lecture lecture = this.lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId.toString()));

        try {
            for(int i = 0; i < fileAttach.length; i++) {
                MultipartFile file = fileAttach[i];
                String fileUrl = fileStorageService.uploadFile(attachFiles, file);
                String originalVideoName = file.getOriginalFilename();
                String extension = FileUtils.getExtensionFile(fileUrl);
                LectureFileAttach lectureFileAttach = new LectureFileAttach();
                lectureFileAttach.setFileUrl(fileUrl);
                lectureFileAttach.setFileSize(file.getSize());
                lectureFileAttach.setFileType(extension);
                lectureFileAttach.setLecture(lecture);
                lectureFileAttach.setFileName(originalVideoName);
                this.lectureFileAttachRepository.save(lectureFileAttach);
            }
            return true;
        } catch (IOException e) {
            throw new UploadFileException("Upload failed!");
        }
    }

    @Override
    public void deleteLectureFileAttach(UUID fileId) {
        LectureFileAttach fileAttach = lectureFileAttachRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("LectureFileAttach", "Id", fileId));
        this.lectureFileAttachRepository.delete(fileAttach);
    }

    @Override
    public void deleteLecture(UUID lectureId) {
        Lecture lecture = this.lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture", "Id", lectureId));
        this.lectureRepository.delete(lecture);
    }
}
