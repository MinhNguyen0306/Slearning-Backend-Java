package com.example.Slearning.Backend.Java.domain.entities;

import com.example.Slearning.Backend.Java.utils.enums.AdminPaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.*;

@Entity
@Table(name = "admin_payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPayment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "mentor_id", referencedColumnName = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    private AdminPaymentStatus adminPaymentStatus;

    @JsonFormat(pattern = "MM-yyyy")
    private YearMonth monthOfYear;

    private double amount;

    @Column(name = "admin_payment_at")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime paymentAt;
}