package com.example.Slearning.Backend.Java.domain.entities;

import com.example.Slearning.Backend.Java.utils.enums.AdminPaymentStatus;
import com.example.Slearning.Backend.Java.utils.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "payments")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE payments SET deleted = true WHERE payment_id = ?")
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    private long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_payment_course_status")
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
