package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.configs.AppConstants;
import com.example.Slearning.Backend.Java.domain.dtos.CourseDto;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import com.example.Slearning.Backend.Java.services.CourseService;
import com.example.Slearning.Backend.Java.utils.enums.CourseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<PageResponse<CourseDto>> getAllCourses(
        @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
        @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ) {
        PageResponse<CourseDto> pageResponse = this.courseService.getAllCourses(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<PageResponse<CourseDto>> filterCoursesByStatus(
        @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
        @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
        @RequestParam(value = "status", required = false) CourseStatus courseStatus
    ) {
        PageResponse<CourseDto> pageResponse = this.courseService.filterCoursesByStatus(pageNumber, pageSize, sortBy, sortDir, courseStatus);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<PageResponse<CourseDto>> filterCoursesByRating(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
            @RequestParam(value = "rating", required = false) Integer rating
    ) {
        PageResponse<CourseDto> pageResponse = this.courseService.filterCoursesByRating(pageNumber, pageSize, sortBy, sortDir, rating);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<PageResponse<CourseDto>> filterCoursesByTopic(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
            @RequestParam(value = "status", required = false) List<UUID> topicIdList
    ) {
        PageResponse<CourseDto> pageResponse = this.courseService.filterCoursesByTopic(pageNumber, pageSize, sortBy, sortDir, topicIdList);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<PageResponse<CourseDto>> filterCoursesByLevel(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
            @RequestParam(value = "status", required = false) List<UUID> levelIdList
    ) {
        PageResponse<CourseDto> pageResponse = this.courseService.filterCoursesByLevel(pageNumber, pageSize, sortBy, sortDir, levelIdList);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<PageResponse<CourseDto>> filterCoursesByPrice(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
            @RequestParam(value = "fromPrice") Integer fromPrice,
            @RequestParam(value = "toPrice") Integer toPrice
    ) {
        PageResponse<CourseDto> pageResponse = this.courseService.filterCoursesByPrice(pageNumber, pageSize, sortBy, sortDir, fromPrice, toPrice);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<CourseDto> createCourse(
        @RequestParam("userId") UUID userId,
        @RequestParam("topicId") UUID topicId,
        @RequestParam("levelId") UUID levelId,
        @RequestParam("imageCourse") MultipartFile imageCourse,
        @RequestBody @Valid CourseDto courseDto
    ) {
        CourseDto createdCourse = this.courseService.createCourse(userId, topicId, levelId, imageCourse, courseDto);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable UUID courseId) {
        CourseDto course = this.courseService.getCourseById(courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PutMapping("/update/{courseId}")
    public ResponseEntity<CourseDto> updateCourse(@RequestBody @Valid CourseDto courseDto, @PathVariable UUID courseId) {
        CourseDto updatedCourse = this.courseService.updateCourse(courseDto, courseId);
        return new ResponseEntity<>(updatedCourse, HttpStatus.CREATED);
    }

    @PatchMapping("/publishing/{courseId}")
    public ResponseEntity<CourseDto> unPublishedCourse(@PathVariable UUID courseId) {
        CourseDto updatedCourse = this.courseService.unPublishedCourse(courseId);
        return new ResponseEntity<>(updatedCourse, HttpStatus.CREATED);
    }

    @PatchMapping("/resolving/{courseId}")
    public ResponseEntity<CourseDto> resolveCourse(
        @RequestParam("courseStatus") CourseStatus courseStatus,
        @PathVariable UUID courseId
    ) {
        CourseDto updatedCourse = this.courseService.resolveCourse(courseStatus, courseId);
        return new ResponseEntity<>(updatedCourse, HttpStatus.CREATED);
    }
}
