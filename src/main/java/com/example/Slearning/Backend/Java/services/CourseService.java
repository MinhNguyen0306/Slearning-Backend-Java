package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.dtos.CourseDto;
import com.example.Slearning.Backend.Java.domain.dtos.UserDto;
import com.example.Slearning.Backend.Java.domain.entities.Course;
import com.example.Slearning.Backend.Java.domain.entities.CourseRating;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import com.example.Slearning.Backend.Java.utils.enums.AdminFetchCourseState;
import com.example.Slearning.Backend.Java.utils.enums.AdminFetchUserState;
import com.example.Slearning.Backend.Java.utils.enums.CourseStatus;
import com.example.Slearning.Backend.Java.utils.enums.ResolveStatus;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface CourseService {

    CourseDto getCourseOfPayment(UUID paymentId);

    PageResponse<CourseDto> searchCourses(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            String searchKey
    );

    CourseDto createDraft(UUID userId, String title);

    String updateTitle(UUID courseId, String title);

    Double updatePrice(UUID courseId, Double price);

    String updateIntro(UUID courseId, String intro);

    String updateDescription(UUID courseId, String description);

    String updateAchievement(UUID courseId, String achievement);

    String updateRequirement(UUID courseId, String requirement);

    ApiResponse updateImageCourse(UUID courseId, MultipartFile imageFile);

    ApiResponse updateCourseTopic(UUID courseId, UUID topicId);

    ApiResponse updateCourseLevel(UUID courseId, Integer levelId);

    ApiResponse publishCourse(UUID courseId);

    PageResponse<CourseDto> getAllCourses(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir
    );

    PageResponse<CourseDto> getCoursesByAdminFetchState(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            AdminFetchCourseState adminFetchCourseState
    );

    PageResponse<CourseDto> filterCoursesByStatus(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            UUID userId,
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
            Integer levelId,
            MultipartFile imageCourse,
            CourseDto courseDto
    );

    List<CourseDto> searchByKeyword(String searchKey);

    CourseDto getCourseById(UUID courseId);

    CourseDto getCoursePublishingById(UUID courseId);

    CourseDto updateCourse(CourseDto courseDto, UUID courseId);

    void deleteCourse(UUID courseID);

    CourseDto unPublishedCourse(UUID courseId);

    ApiResponse resolveCourse(ResolveStatus resolveStatus, UUID courseId);

    ApiResponse ratingCourse(UUID userId, UUID courseId, Integer rating, String comment);

    List<CourseRating> getRatingsOfCourse(UUID courseId);
}
