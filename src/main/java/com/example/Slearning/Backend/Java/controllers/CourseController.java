package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.configs.AppConstants;
import com.example.Slearning.Backend.Java.domain.dtos.CourseDto;
import com.example.Slearning.Backend.Java.domain.dtos.UserDto;
import com.example.Slearning.Backend.Java.domain.entities.CourseRating;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import com.example.Slearning.Backend.Java.services.CourseService;
import com.example.Slearning.Backend.Java.utils.enums.AdminFetchCourseState;
import com.example.Slearning.Backend.Java.utils.enums.AdminFetchUserState;
import com.example.Slearning.Backend.Java.utils.enums.CourseStatus;
import com.example.Slearning.Backend.Java.utils.enums.ResolveStatus;
import com.google.protobuf.Api;
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

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<CourseDto> getCourseOfPayment(@PathVariable UUID paymentId) {
        CourseDto courseDto = this.courseService.getCourseOfPayment(paymentId);
        return ResponseEntity.ok(courseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<CourseDto>> searchCourses(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
            @RequestParam("searchKey") String searchKey
    ) {
        PageResponse<CourseDto> result = this.courseService.searchCourses(pageNumber, pageSize, sortBy, sortDir, searchKey);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create/draft")
    public ResponseEntity<CourseDto> createDraft(
            @RequestParam("userId") UUID userId,
            @RequestParam("title") String title
    ) {
        CourseDto createdCourse = this.courseService.createDraft(userId, title);
        if(createdCourse == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @PatchMapping("/{courseId}/update/title")
    public ResponseEntity<String> updateTitle(
            @PathVariable UUID courseId,
            @RequestParam("title") String title
    ) {
        String updatedTitle = this.courseService.updateTitle(courseId, title);
        if(updatedTitle == "") {
            return ResponseEntity.badRequest().body("Cap nhat that bai");
        }
        return new ResponseEntity<>(title, HttpStatus.CREATED);
    }

    @PatchMapping("/{courseId}/update/price")
    public ResponseEntity<Double> updatePrice(
            @PathVariable UUID courseId,
            @RequestParam("price") Double price
    ) {
        Double updatedPrice = this.courseService.updatePrice(courseId, price);
        return new ResponseEntity<>(updatedPrice, HttpStatus.CREATED);
    }

    @PatchMapping("/{courseId}/update/introduce")
    public ResponseEntity<String> updateIntro(
            @PathVariable UUID courseId,
            @RequestParam("introduce") String introduce
    ) {
        String updatedIntro = this.courseService.updateIntro(courseId, introduce);
        if(updatedIntro == "") {
            return ResponseEntity.badRequest().body("Cap nhat that bai");
        }
        return new ResponseEntity<>(introduce, HttpStatus.CREATED);
    }

    @PatchMapping("/{courseId}/update/description")
    public ResponseEntity<String> updateDescription(
            @PathVariable UUID courseId,
            @RequestParam("description") String description
    ) {
        String updateDescription = this.courseService.updateDescription(courseId, description);
        if(updateDescription == "") {
            return ResponseEntity.badRequest().body("Cap nhat that bai");
        }
        return new ResponseEntity<>(description, HttpStatus.CREATED);
    }

    @PatchMapping("/{courseId}/update/achievement")
    public ResponseEntity<String> updateAchievement(
            @PathVariable UUID courseId,
            @RequestParam("achievement") String achievement
    ) {
        String updatedAchievement = this.courseService.updateAchievement(courseId, achievement);
        if(updatedAchievement == "") {
            return ResponseEntity.badRequest().body("Cap nhat that bai");
        }
        return new ResponseEntity<>(achievement, HttpStatus.CREATED);
    }

    @PatchMapping("/{courseId}/update/requirement")
    public ResponseEntity<String> updateRequirement(
            @PathVariable UUID courseId,
            @RequestParam("requirement") String requirement
    ) {
        String updatedRequire = this.courseService.updateRequirement(courseId, requirement);
        if(updatedRequire == "") {
            return ResponseEntity.badRequest().body("Cap nhat that bai");
        }
        return new ResponseEntity<>(requirement, HttpStatus.CREATED);
    }

    @PutMapping("/{courseId}/update/image")
    public ResponseEntity<ApiResponse> updateImageCourse(
            @PathVariable UUID courseId,
            @RequestPart("image") MultipartFile image
    ) {
        ApiResponse updatedImage = this.courseService.updateImageCourse(courseId, image);
        if(updatedImage.getStatus() == "500") {
            return ResponseEntity.internalServerError().body(updatedImage);
        }
        return new ResponseEntity<>(updatedImage, HttpStatus.CREATED);
    }

    @PutMapping("/{courseId}/update/topic")
    public ResponseEntity<ApiResponse> updateCourseTopic(
            @PathVariable UUID courseId,
            @RequestParam("topicId") UUID topicId
    ) {
        ApiResponse updatedTopic = this.courseService.updateCourseTopic(courseId, topicId);
        if(updatedTopic.getStatus() == "500") {
            return ResponseEntity.internalServerError().body(updatedTopic);
        }
        return new ResponseEntity<>(updatedTopic, HttpStatus.CREATED);
    }

    @PutMapping("/{courseId}/update/level")
    public ResponseEntity<ApiResponse> updateCourseLevel(
            @PathVariable UUID courseId,
            @RequestParam("levelId") Integer levelId
    ) {
        ApiResponse updatedLevel = this.courseService.updateCourseLevel(courseId, levelId);
        if(updatedLevel.getStatus() == "500") {
            return ResponseEntity.internalServerError().body(updatedLevel);
        }
        return new ResponseEntity<>(updatedLevel, HttpStatus.CREATED);
    }

    @PutMapping("/{courseId}/request-publish")
    public ResponseEntity<ApiResponse> publishCourse(@PathVariable UUID courseId) {
        ApiResponse response = this.courseService.publishCourse(courseId);
        return ResponseEntity.ok(response);
    }

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

    @GetMapping("/fetch-state")
    public ResponseEntity<PageResponse<CourseDto>> getCoursesByAdminFetchState(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
            @RequestParam(value = "state") AdminFetchCourseState adminFetchCourseState
    ) {
        PageResponse<CourseDto> pageResponse = this.courseService.getCoursesByAdminFetchState(
                pageNumber, pageSize, sortBy, sortDir, adminFetchCourseState
        );
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/status")
    public ResponseEntity<PageResponse<CourseDto>> filterCoursesByStatus(
        @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
        @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
        @RequestParam(value = "userId", required = false) UUID userId,
        @RequestParam(value = "status", required = false) CourseStatus courseStatus
    ) {
        PageResponse<CourseDto> pageResponse = this.courseService.filterCoursesByStatus(
                pageNumber, pageSize, sortBy, sortDir, userId, courseStatus
        );
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/rating")
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

    @GetMapping("/topic")
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

    @GetMapping("/level")
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

    @GetMapping("/price")
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
        @RequestParam("levelId") Integer levelId,
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

    @GetMapping("/{courseId}/publishing")
    public ResponseEntity<CourseDto> getCoursePublishingById(@PathVariable UUID courseId) {
        CourseDto course = this.courseService.getCoursePublishingById(courseId);
        if(course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(course);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseDto> updateCourse(@RequestBody @Valid CourseDto courseDto, @PathVariable UUID courseId) {
        CourseDto updatedCourse = this.courseService.updateCourse(courseDto, courseId);
        return new ResponseEntity<>(updatedCourse, HttpStatus.CREATED);
    }

    @PatchMapping("/publishing/{courseId}")
    public ResponseEntity<CourseDto> unPublishedCourse(@PathVariable UUID courseId) {
        CourseDto updatedCourse = this.courseService.unPublishedCourse(courseId);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    @PatchMapping("/resolving/{courseId}")
    public ResponseEntity<ApiResponse> resolveCourse(
            @PathVariable UUID courseId,
            @RequestParam("status") ResolveStatus resolveStatus
    ) {
        ApiResponse response = this.courseService.resolveCourse(resolveStatus, courseId);
        if(response.getStatus() == "400") {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/rating")
    public ResponseEntity<ApiResponse> ratingCourse(
            @RequestParam("userId") UUID userId,
            @RequestParam("courseId") UUID courseId,
            @RequestParam("rating") Integer rating,
            @RequestParam(value = "comment", required = false) String comment
    ) {
        ApiResponse response = this.courseService.ratingCourse(userId, courseId, rating, comment);
        if(response.getStatus() == "201") {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{courseId}/ratings")
    public ResponseEntity<List<CourseRating>> getRatingsOfCourse(@PathVariable UUID courseId) {
        List<CourseRating> courseRatings = this.courseService.getRatingsOfCourse(courseId);
        return ResponseEntity.ok(courseRatings);
    }
}
