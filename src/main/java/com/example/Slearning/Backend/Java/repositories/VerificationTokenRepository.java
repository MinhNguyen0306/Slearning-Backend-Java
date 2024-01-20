package com.example.Slearning.Backend.Java.repositories;

import com.example.Slearning.Backend.Java.domain.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {

    @Query("SELECT t FROM VerificationToken t WHERE t.tokenValue = ?1")
    Optional<VerificationToken> findByToken(String token);
}
