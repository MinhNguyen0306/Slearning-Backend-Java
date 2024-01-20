package com.example.Slearning.Backend.Java.repositories;

import com.example.Slearning.Backend.Java.domain.entities.Course;
import com.example.Slearning.Backend.Java.domain.entities.Payment;
import com.example.Slearning.Backend.Java.domain.entities.Progress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, UUID> {

    @Query("SELECT c FROM Course c, Payment p " +
            "WHERE p.course.id = c.id " +
            "AND p.user.id = :userId " +
            "AND p.paymentStatus = SUCCESS ")
    Page<Course> getCoursesUserHasPayment(Pageable pageable, @Param("userId") UUID userId);

    @Query("SELECT p FROM Progress p " +
            "WHERE p.lecture.chapter.course.id = :courseId " +
            "AND p.user.id = :userId")
    List<Progress> getProgressCourseOfUser(@Param("userId") UUID userId, @Param("courseId") UUID courseId);

    @Query("SELECT p FROM Progress p " +
            "WHERE p.user.id = :userId " +
            "AND p.lecture.chapter.course.id = :courseId " +
            "AND p.isCompleted = false")
    Optional<Progress> getCurrentProgress(@Param("userId") UUID userId, @Param("courseId") UUID courseId);

}
