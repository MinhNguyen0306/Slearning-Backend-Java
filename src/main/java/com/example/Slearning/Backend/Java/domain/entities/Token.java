package com.example.Slearning.Backend.Java.domain.entities;

import com.example.Slearning.Backend.Java.utils.enums.TokenType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tokens")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private int id;

    @Column(name = "token_value", nullable = false)
    private String value;

    @Column(name = "token_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType type;

    @CreationTimestamp
    @JsonFormat(pattern = "dd-MM-yyy hh:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    public void dismissUser() {
        this.user.dismissToken(this);
        this.user = null;
    }
}
