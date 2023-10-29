package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
public class Payment extends BaseEntity {

    @OneToOne
    @MapsId
    private Enroll enroll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monthly_payment_id")
    private MonthlyPayment monthlyPayment;
}
