package com.example.Slearning.Backend.Java.repositories;

import com.example.Slearning.Backend.Java.domain.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
}
