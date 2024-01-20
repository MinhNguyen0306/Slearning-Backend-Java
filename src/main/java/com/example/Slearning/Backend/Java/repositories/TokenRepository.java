package com.example.Slearning.Backend.Java.repositories;

import com.example.Slearning.Backend.Java.domain.entities.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends CrudRepository<Token, Integer> {

    @Query(value = "select * from tokens where tokens.user_id = :userId", nativeQuery = true)
    Optional<Token> findTokenByUser(UUID userId);
}
