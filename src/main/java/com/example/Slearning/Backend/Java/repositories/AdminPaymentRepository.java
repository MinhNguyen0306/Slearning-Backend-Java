package com.example.Slearning.Backend.Java.repositories;

import com.example.Slearning.Backend.Java.domain.entities.AdminPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminPaymentRepository extends JpaRepository<AdminPayment, UUID> {

    @Query(value = "SELECT * FROM admin_payment WHERE month_of_year = ?1", nativeQuery = true)
    Optional<AdminPayment> checkAdminPayment(String yearMonth);

    @Query("SELECT p FROM AdminPayment p " +
            "WHERE p.user.id = :userId ")
    Page<AdminPayment> getAllAdminPaymentsOfUser(Pageable pageable, @Param("userId") UUID userId);

    @Query("SELECT p FROM AdminPayment p " +
            "WHERE p.adminPaymentStatus = PENDING")
    Page<AdminPayment> getPendingAdminPayment(Pageable pageable);

    @Query("SELECT p FROM AdminPayment p " +
            "WHERE p.adminPaymentStatus = SUCCESS")
    Page<AdminPayment> getSuccessAdminPayment(Pageable pageable);
}
