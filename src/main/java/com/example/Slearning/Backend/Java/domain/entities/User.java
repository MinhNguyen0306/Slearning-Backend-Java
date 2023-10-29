package com.example.Slearning.Backend.Java.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE users SET deleted = true where user_id = ?")
@Data
@NoArgsConstructor @AllArgsConstructor
public class User extends BaseEntity implements UserDetails {

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
    @Where(clause = "isInstructor='true'")
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


    // Auth Entity
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
