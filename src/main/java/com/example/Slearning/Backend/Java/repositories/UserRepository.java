package com.example.Slearning.Backend.Java.repositories;

import com.example.Slearning.Backend.Java.domain.entities.Payment;
import com.example.Slearning.Backend.Java.domain.entities.User;
import com.example.Slearning.Backend.Java.utils.enums.AdminFetchUserState;
import com.example.Slearning.Backend.Java.utils.enums.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.userStatus = :status")
    Page<User> filterUserByStatus(Pageable pageable, @Param(value = "status") UserStatus userStatus);

    @Query("SELECT u FROM User u WHERE u.isInstructor = true")
    Page<User> filterUserIsInstructor(Pageable pageable);
}
