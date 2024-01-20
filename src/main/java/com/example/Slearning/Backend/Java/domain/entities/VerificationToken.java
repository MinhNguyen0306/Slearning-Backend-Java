package com.example.Slearning.Backend.Java.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String tokenValue;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    @Column(nullable = false)
    private LocalDateTime createAt;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
