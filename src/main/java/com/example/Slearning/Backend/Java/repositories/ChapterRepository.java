package com.example.Slearning.Backend.Java.repositories;

import com.example.Slearning.Backend.Java.domain.entities.Chapter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChapterRepository extends CrudRepository<Chapter, UUID> {

    @Query("SELECT c FROM Chapter c, Lecture l WHERE c.id = l.chapter.id AND l.id = :lectureId")
    Optional<Chapter> getChapterOfLecture(@Param("lectureId") UUID lectureId);
}
