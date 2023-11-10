package com.example.Slearning.Backend.Java.repositories;

import com.example.Slearning.Backend.Java.domain.entities.Course;
import com.example.Slearning.Backend.Java.utils.enums.CourseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    @Query(value = "SELECT c FROM Course c WHERE c.courseStatus = ?1")
    Page<Course> filterCourseByStatus(Pageable pageable, CourseStatus courseStatus);

    @Query(value = "select * from courses where course in " +
            "(select * from course_ratings join courses on courses.id = course_ratings.course_id " +
            "where user_rating_course >= ?1)", nativeQuery = true)
    Page<Course> filterCourseByRating(Pageable pageable, Integer rating);

    @Query(value = "SELECT c FROM Course c JOIN User u ON c.user_id = u.id " +
            "WHERE c.courseTitle LIKE %:searchKey% OR u.fullName LIKE %:searchKey%")
    List<Course> searchCourseByTitle(@Param(value = "searchKey") String searchKey);
}
