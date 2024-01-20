package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.domain.dtos.LectureDto;
import com.example.Slearning.Backend.Java.domain.entities.LectureFileAttach;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.services.LectureService;
import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;
import com.google.protobuf.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    @PostMapping
    public ResponseEntity<LectureDto> createLecture(
            @RequestParam("chapterId") UUID chapterId,
            @RequestParam("title") String title,
            @RequestParam(value = "description", defaultValue = "", required = false) String description
    ) {
        LectureDto lectureDto = lectureService.createLecture(chapterId, title, description);
        if(lectureDto == null) {
            return ResponseEntity.internalServerError().build();
        }

        return new ResponseEntity<>(lectureDto, HttpStatus.CREATED);
    }

    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureDto> getLectureById(
            @PathVariable UUID lectureId,
            @RequestParam(value = "status", required = false) PublishStatus publishStatus
    ) {
        LectureDto lectureDto = lectureService.getLectureById(lectureId, publishStatus);
        if(lectureDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lectureDto);
    }

    @PostMapping("/{lectureId}/upload-video")
    public ResponseEntity<LectureDto> uploadLectureVideo(
            @PathVariable UUID lectureId,
            @RequestPart("video") MultipartFile lectureVideo,
            @RequestParam("videoDuration") Double videoDuration
    ) {
        LectureDto uploaded = lectureService.uploadLectureVideo(lectureId, lectureVideo, videoDuration);
        if(uploaded == null) {
            return ResponseEntity.internalServerError().build();
        }
        return new ResponseEntity<>(uploaded, HttpStatus.CREATED);
    }

    @GetMapping("/{lectureId}/file-attach")
    public ResponseEntity<List<LectureFileAttach>> getFilesAttachOfLecture(
            @PathVariable UUID lectureId
    ) {
        List<LectureFileAttach> lectureFileAttaches = lectureService.getFilesAttachOfLecture(lectureId);
        return ResponseEntity.ok(lectureFileAttaches);
    }

    @PostMapping("/{lectureId}/upload-file-attach")
    public ResponseEntity<Boolean> uploadLectureFileAttach(
            @PathVariable UUID lectureId,
            @RequestPart("files") MultipartFile[] multipartFiles
    ) {
        boolean uploaded = lectureService.uploadLectureFileAttach(lectureId, multipartFiles);
        return new ResponseEntity<>(uploaded, HttpStatus.CREATED);
    }

    @DeleteMapping("/{fileAttachId}")
    public ResponseEntity<Void> deleteLectureFileAttach(@PathVariable UUID fileAttachId) {
        lectureService.deleteLectureFileAttach(fileAttachId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{lectureId}/previewed")
    public ResponseEntity<Boolean> updatePreviewed(@PathVariable UUID lectureId) {
        boolean response = lectureService.updatePreviewed(lectureId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{lectureId}/publishing")
    public ResponseEntity<PublishStatus> updatePublishing(@PathVariable UUID lectureId) {
        PublishStatus response = lectureService.updatePublishing(lectureId);
        return ResponseEntity.ok(response);
    }
}
