package com.example.Slearning.Backend.Java.domain.entities;

import com.example.Slearning.Backend.Java.utils.enums.VnpayPaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VnpayPayment {

    @Id
    private String id;

    private long amount;

    @Enumerated(EnumType.STRING)
    private VnpayPaymentStatus vnpayPaymentStatus;

    private UUID userId;
    private UUID courseId;
    private UUID adminPaymentId;
}
