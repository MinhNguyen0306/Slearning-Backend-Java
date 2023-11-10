package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.dtos.CourseDto;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import com.example.Slearning.Backend.Java.utils.enums.CourseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    PageResponse<CourseDto> getAllCourses(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir
    );
    PageResponse<CourseDto> filterCoursesByStatus(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            CourseStatus courseStatus
    );
    PageResponse<CourseDto> filterCoursesByRating(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            Integer rating
    );
    PageResponse<CourseDto> filterCoursesByTopic(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            List<UUID> topicIdList
    );
    PageResponse<CourseDto> filterCoursesByLevel(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            List<UUID> levelIdList
    );
    PageResponse<CourseDto> filterCoursesByPrice(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            Integer fromPrice,
            Integer toPrice
    );
    CourseDto createCourse(
            UUID userId,
            UUID topicId,
            UUID levelId,
            MultipartFile imageCourse,
            CourseDto courseDto
    );
    List<CourseDto> searchByKeyword(String searchKey);
    CourseDto getCourseById(UUID courseId);
    CourseDto updateCourse(CourseDto courseDto, UUID courseId);
    void deleteCourse(UUID courseID);
    CourseDto unPublishedCourse(UUID courseId);
    CourseDto resolveCourse(CourseStatus courseStatus, UUID courseId);
}
