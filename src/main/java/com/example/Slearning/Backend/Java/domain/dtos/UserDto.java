package com.example.Slearning.Backend.Java.domain.dtos;

import lombok.Data;

import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private String email;
    private String password;
    private String fullName;
    private Integer age;
    private String about;
    private String education;
    private Blob avatar;
    private LocalDate lastLogin;
    private boolean isLock = false;
    private boolean isInstructor = false;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
