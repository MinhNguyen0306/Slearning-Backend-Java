package com.example.Slearning.Backend.Java.domain.entities;

import com.example.Slearning.Backend.Java.utils.enums.DeviceType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String tokenValue;

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm::ss")
    private LocalDateTime createAt;

    @JsonFormat(pattern = "dd-MM-yyyy hh:mm::ss")
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void dismissUser() {
        this.user.dismissDeviceToken(this);
        this.user = null;
    }
}
