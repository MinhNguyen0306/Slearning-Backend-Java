package com.example.Slearning.Backend.Java.repositories;

import com.example.Slearning.Backend.Java.domain.entities.CourseRating;
import com.example.Slearning.Backend.Java.domain.entities.CourseRatingKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRatingRepository extends CrudRepository<CourseRating, CourseRatingKey> {

    @Query(value = "SELECT * FROM course_ratings r " +
            "WHERE r.user_id = ?1 " +
            "AND r.course_id = ?2 ", nativeQuery = true)
    Optional<CourseRating> findCourseRating(UUID userId, UUID courseId);

    @Query(value = "SELECT * FROM course_ratings r " +
            "WHERE r.course_id = ?1 ", nativeQuery = true)
    List<CourseRating> getRatingsOfCourse(UUID courseId);
}
