package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.domain.dtos.ChapterDto;
import com.example.Slearning.Backend.Java.services.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/chapters")
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    @PostMapping
    public ResponseEntity<ChapterDto> createChapter(
            @RequestParam("courseId") UUID courseId,
            @RequestParam("title") String title,
            @RequestParam(value = "description", defaultValue = "", required = false) String description
    ) {
        ChapterDto stored = chapterService.createChapter(courseId, title, description);
        if(stored == null) {
            return ResponseEntity.internalServerError().build();
        }

        return new ResponseEntity<>(stored, HttpStatus.CREATED);
    }

    @GetMapping("/{chapterId}")
    public ResponseEntity<ChapterDto> getChapterById(@PathVariable UUID chapterId) {
        ChapterDto chapterDto = chapterService.getChapterById(chapterId);
        return ResponseEntity.ok(chapterDto);
    }

    @GetMapping("/lectures/{lectureId}")
    public ResponseEntity<ChapterDto> getChapterOfLecture(@PathVariable UUID lectureId) {
        ChapterDto chapterDto = chapterService.getChapterOfLecture(lectureId);
        return ResponseEntity.ok(chapterDto);
    }
}
