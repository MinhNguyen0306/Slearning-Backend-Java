package com.example.Slearning.Backend.Java.repositories;

import com.example.Slearning.Backend.Java.domain.entities.TrackingCodingEx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrackingCodingExRepository extends JpaRepository<TrackingCodingEx, Integer> {

    @Query("SELECT t FROM TrackingCodingEx t " +
            "WHERE t.user.id = :userId " +
            "AND t.codingExercise.id = :exId ")
    Optional<TrackingCodingEx> getByUserAndEx(@Param("userId") UUID userId, @Param("exId") Integer exId);
}
