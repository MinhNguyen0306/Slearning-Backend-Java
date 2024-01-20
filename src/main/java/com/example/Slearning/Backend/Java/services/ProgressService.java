package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.dtos.CourseDto;
import com.example.Slearning.Backend.Java.domain.entities.Progress;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;

import java.util.List;
import java.util.UUID;

public interface ProgressService {

    List<Progress> getProgressCourseOfUser(UUID userId, UUID courseId);

    PageResponse<CourseDto> getMyLearning(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            UUID userId
    );

    Progress getCurrentProgress(UUID userId, UUID courseId);

    Progress getNextProgress(UUID userId, UUID courseId, UUID lectureId, Integer grade);

    Progress getProgressOfLecture(UUID userId , UUID lectureId);

    boolean checkOpenTest(UUID userId, UUID chapterId);
}
