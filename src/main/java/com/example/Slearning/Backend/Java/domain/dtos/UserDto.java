package com.example.Slearning.Backend.Java.domain.dtos;

import com.example.Slearning.Backend.Java.domain.entities.*;
import com.example.Slearning.Backend.Java.utils.enums.UserStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String email;
    private String fullName;
    private Integer age;
    private String about;
    private String education;
    private ImageStorage avatar;
    private LocalDate lastLogin;
    private boolean isLock;
//    private List<CourseRating> courseRatings;
    private boolean isInstructor;
    private AccountBalance accountBalance;
    private String phone;
    private LocalDateTime dateRegisterInstructor;
    private List<Course> courses;
    private List<Payment> payments;
    private List<AdminPayment> adminPayments;
    private List<WorkExperience> workExperiences;
    private UserStatus userStatus;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<DeviceToken> deviceTokens;
    private List<Role> roles;
}
