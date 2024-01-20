package com.example.Slearning.Backend.Java.repositories;

import com.example.Slearning.Backend.Java.domain.entities.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, UUID> {

    @Query(value = "SELECT * FROM (SELECT * FROM lectures l WHERE l.chapter_id = :chapterId ORDER BY l.lecture_position ASC) as ll " +
            "WHERE ll.lecture_position > :prevPosition AND ll.lecture_publish_status = 'PUBLISHING' ORDER BY ll.lecture_position ASC LIMIT 1", nativeQuery = true)
    Optional<Lecture> getNextLecture(@Param("chapterId") UUID chapterId, @Param("prevPosition") Integer prevPosition);
}
