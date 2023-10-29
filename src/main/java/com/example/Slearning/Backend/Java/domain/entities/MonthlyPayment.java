package com.example.Slearning.Backend.Java.domain.entities;

import com.example.Slearning.Backend.Java.utils.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;

@Entity
@Table(name = "monthly_payments")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE monthly_payments SET deleted = true WHERE monthly_payment_id = ?")
@Data
@NoArgsConstructor
public class MonthlyPayment extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "monthly_payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "payment_for_month", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate month;

    @Column(name = "payment_for_year", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate year;

    @Column(name = "admin_payment_at")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime paymentAt;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public int getMonth() {
        return month.getMonthValue();
    }

    public int getYear() {
        return year.getYear();
    }
}
