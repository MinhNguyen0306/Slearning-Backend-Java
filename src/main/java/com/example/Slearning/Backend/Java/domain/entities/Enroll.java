package com.example.Slearning.Backend.Java.domain.entities;

import com.example.Slearning.Backend.Java.utils.enums.EnrollStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "enrolls")
@Where(clause = "deleted=false")
@SQLDelete(sql = "UPDATE enrolls SET deleted = true WHERE enroll_id = ?")
@Data
@NoArgsConstructor
public class Enroll extends BaseEntity {

    @Column(name = "enroll_status")
    @Enumerated(EnumType.STRING)
    private EnrollStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    @MapsId
    @JsonManagedReference
    private Course course;

    @OneToOne
    @MapsId
    private Progress progress;

    @OneToOne
    @MapsId
    private Payment payment;
}
