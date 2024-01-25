package com.example.Slearning.Backend.Java.repositories;

import com.example.Slearning.Backend.Java.domain.entities.Payment;
import com.example.Slearning.Backend.Java.utils.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    @Query("SELECT CASE WHEN (p.amount = :amount) THEN true ELSE false END FROM Payment p WHERE p.id = :paymentId")
    Boolean checkAmount(@Param("paymentId") UUID paymentId, @Param("amount") long amount);

    @Query("SELECT CASE WHEN (p.paymentStatus = :status) THEN true ELSE false END FROM Payment p WHERE p.id = :paymentId")
    Boolean checkPaymentStatus(@Param("paymentId") UUID paymentId, @Param("status")PaymentStatus paymentStatus);

    @Query("SELECT p FROM Payment p " +
            "WHERE p.user.id = :userId " +
            "AND p.course.id = :courseId " +
            "AND p.paymentStatus = SUCCESS ")
    Optional<Payment> getPaymentOfCourse(@Param("userId") UUID userId, @Param("courseId") UUID courseId);

    @Query("SELECT p FROM Payment p " +
            "WHERE p.user.id = :userId " +
            "AND p.paymentStatus = SUCCESS ")
    Page<Payment> getPaymentsOfUser(Pageable pageable, @Param("userId") UUID userId);

    @Query("SELECT SUM(p.amount*0.95) FROM Payment p " +
            "WHERE p.course.user.id = :mentorId ")
    Double getCurrentRevenueOfMentor(@Param("mentorId") UUID mentorId);

    @Query("SELECT p FROM Payment p " +
            "WHERE p.course.user.id = ?1")
    List<Payment> getPaymentsOfMentor(UUID mentorId);
}
