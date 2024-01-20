package com.example.Slearning.Backend.Java.domain.entities;

import com.example.Slearning.Backend.Java.utils.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE users SET deleted = true where user_id = ?")
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {

    @Email(message = "Email not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email can not empty")
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Size(max = 50, min = 5, message = "Full name length not valid")
    private String fullName;

    private Integer age;

    @Size(min = 200, message = "It nhat 200 ky tu")
    @Max(value = 1000, message = "Khong duoc qua 1000 ky tu")
    @Column(length = 1000)
    private String about;

    private String education;

    @OneToOne
    @JoinColumn(name = "avatar", referencedColumnName = "id")
    private ImageStorage avatar;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDate lastLogin;

    private boolean isLock;

    private boolean isEnabled;

    private boolean isInstructor;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(name = "instructor_phone")
    @Pattern(regexp = "^(0|\\\\+84)(\\\\s|\\\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))" +
            "(\\\\d)(\\\\s|\\\\.)?(\\\\d{3})(\\\\s|\\\\.)?(\\\\d{3})$")
    @Size(min = 9, max = 11, message = "Phone number invalid")
    private String phone;

    @Column(name = "instructor_date_regis")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime dateRegisterInstructor;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private AccountBalance accountBalance;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payment> payments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AdminPayment> adminPayments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkExperience> workExperiences;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Progress> progresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CalendarEvent> calendarEvents;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Token> tokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DeviceToken> deviceTokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<VerificationToken> verificationTokens;

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

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void addWorkExperience(WorkExperience workExperience) {
        this.workExperiences.add(workExperience);
    }

    public void dismissToken(Token token) {
        this.tokens.remove(token);
    }

    public void dismissDeviceToken(DeviceToken deviceToken) {
        this.deviceTokens.remove(deviceToken);
    }
}
