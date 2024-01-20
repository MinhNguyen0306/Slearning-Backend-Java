package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.configs.AppConstants;
import com.example.Slearning.Backend.Java.domain.dtos.CourseDto;
import com.example.Slearning.Backend.Java.domain.entities.Progress;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import com.example.Slearning.Backend.Java.services.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @GetMapping("/user/{userId}/course/{courseId}")
    public ResponseEntity<List<Progress>> getProgressCourseOfUser(
            @PathVariable UUID userId,
            @PathVariable UUID courseId
    ) {
        List<Progress> progresses = progressService.getProgressCourseOfUser(userId, courseId);
        return ResponseEntity.ok(progresses);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PageResponse<CourseDto>> getMyLearning(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
            @PathVariable("userId") UUID userId
    ) {
        PageResponse<CourseDto> response = this.progressService.getMyLearning(pageNumber, pageSize, sortBy, sortDir, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/course/{courseId}")
    public ResponseEntity<Progress> getCurrentProgress(
            @PathVariable UUID userId,
            @PathVariable UUID courseId
    ) {
        Progress progress = this.progressService.getCurrentProgress(userId, courseId);
        return ResponseEntity.ok(progress);
    }

    @PostMapping("/{userId}/course/{courseId}/next")
    public ResponseEntity<Progress> getNextProgress(
            @PathVariable UUID userId,
            @PathVariable UUID courseId,
            @RequestParam("lectureId") UUID lectureId,
            @RequestParam(value = "grade", required = false) Integer grade
    ) {
        Progress nextProgress = this.progressService.getNextProgress(userId, courseId, lectureId, grade);
        return new ResponseEntity<>(nextProgress, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/lecture/{lectureId}")
    public ResponseEntity<Progress> getProgressOfLecture(
            @PathVariable UUID userId,
            @PathVariable("lectureId") UUID lectureId
    ) {
        Progress nextProgress = this.progressService.getProgressOfLecture(userId, lectureId);
        return ResponseEntity.ok(nextProgress);
    }

    @GetMapping("/test")
    public ResponseEntity<Boolean> checkOpenTest(
            @RequestParam("userId") UUID userId,
            @RequestParam("chapterId") UUID chapterId
    ) {
        boolean checkToOpen = this.progressService.checkOpenTest(userId, chapterId);
        return ResponseEntity.ok(checkToOpen);
    }
}
