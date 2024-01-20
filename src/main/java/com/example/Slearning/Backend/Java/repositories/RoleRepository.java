package com.example.Slearning.Backend.Java.repositories;

import com.example.Slearning.Backend.Java.domain.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("SELECT r FROM Role r WHERE r.role = ?1")
    Optional<Role> findRoleByName(String role);
}
