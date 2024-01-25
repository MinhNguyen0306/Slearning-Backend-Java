package com.example.Slearning.Backend.Java.repositories;

import com.example.Slearning.Backend.Java.domain.entities.Course;
import com.example.Slearning.Backend.Java.domain.entities.SubCategory;
import com.example.Slearning.Backend.Java.domain.entities.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<Topic, UUID> {
    @Query(value = "SELECT * FROM topics t where t.sub_category_id = :subCategoryId", nativeQuery = true)
    Page<Topic> findTopicOfSubCategory(Pageable pageable, @Param("subCategoryId") UUID subCategoryId);

    @Query("SELECT t.title, COUNT(p.course.id) nCourse FROM Course c, Topic t, Payment p " +
            "WHERE c.id = p.course.id " +
            "AND c.user.id = ?1 " +
            "AND c.topic.id = t.id " +
            "GROUP BY p.course.id " +
            "ORDER By nCourse DESC LIMIT 5 ")
    List<String> top5Topic(UUID mentorId);

    @Query("SELECT COUNT(p.course.id) nCourse FROM Course c, Topic t, Payment p " +
            "WHERE c.id = p.course.id " +
            "AND c.user.id = ?1 " +
            "AND c.topic.id = t.id " +
            "GROUP BY p.course.id " +
            "ORDER By nCourse DESC LIMIT 5 ")
    List<Integer> numberCourseSold(UUID mentorId);
}
