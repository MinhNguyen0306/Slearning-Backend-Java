package com.example.Slearning.Backend.Java.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Table
@Entity
@Data
@NoArgsConstructor
public class AccountBalance {
    @Id
    @JoinColumn(name = "user_id")
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime lastedTransactionDate;

    private Double userBalance;
}
