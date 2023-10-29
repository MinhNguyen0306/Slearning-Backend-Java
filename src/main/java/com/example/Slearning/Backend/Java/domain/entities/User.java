package com.example.Slearning.Backend.Java.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Blob;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE users SET deleted = true where user_id = ?")
public class User extends BaseEntity {

    @Email(message = "Email not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email can not empty")
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Size(max = 50, min = 5, message = "Full name length not valid")
    private String fullName;

    private Integer age;

    @Size(min = 50, message = "About must be at least 50 character")
    private String about;

    private String education;

    @Lob
    private Blob avatar;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDate lastLogin;

    private boolean isLock = false;

    private boolean isInstructor = false;

    @Column(name = "instructor_phone")
    @Pattern(regexp = "^(0|\\\\+84)(\\\\s|\\\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))" +
            "(\\\\d)(\\\\s|\\\\.)?(\\\\d{3})(\\\\s|\\\\.)?(\\\\d{3})$")
    @Size(min = 9, max = 11, message = "Phone number invalid")
    @Where(clause = "isInstructor='true'")
    private String phone;

    @Column(name = "instructor_date_regis")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Where(clause = "isInstructor='true'")
    private LocalDateTime dateRegisterInstructor;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Enroll> enrolls = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CourseRating> courseRatings;

    @ManyToMany
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
}
