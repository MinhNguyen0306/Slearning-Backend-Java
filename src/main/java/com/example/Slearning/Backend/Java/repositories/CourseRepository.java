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
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    @Query("SELECT c FROM Course c, Payment p " +
            "WHERE c.id = p.course.id " +
            "AND p.id = ?1 ")
    Optional<Course> getCourseOfPayment(UUID paymentId);

    @Query(value = "SELECT c FROM Course c WHERE c.status = :status")
    Page<Course> filterCourseByStatus(Pageable pageable, @Param(value = "status") CourseStatus courseStatus);

    @Query(value = "SELECT c FROM Course c WHERE c.status = :status AND c.user.id = :userId")
    Page<Course> filterCourseByStatusOfUser(
            Pageable pageable,
            @Param(value = "userId") UUID userId,
            @Param(value = "status") CourseStatus courseStatus
    );

    // Co loi query nham attribute
    @Query(value = "select * from courses where course in " +
            "(select * from course_ratings join courses on courses.id = course_ratings.course_id " +
            "where user_rating_course >= ?1)", nativeQuery = true)
    Page<Course> filterCourseByRating(Pageable pageable, Integer rating);

    @Query("SELECT c FROM Course c " +
            "WHERE c.status = PUBLISHING " +
            "AND c.price >= :fromPrice " +
            "AND c.price <= :toPrice ")
    Page<Course> filterCoursesByPrice(
            Pageable pageable,
            @Param("fromPrice") Integer fromPrice,
            @Param("toPrice") Integer toPrice
    );

    @Query("SELECT c FROM Course c WHERE c.title LIKE %:searchKey% AND c.status = PUBLISHING")
    Page<Course> searchCourseByTitle(Pageable pageable, @Param(value = "searchKey") String searchKey);
}
