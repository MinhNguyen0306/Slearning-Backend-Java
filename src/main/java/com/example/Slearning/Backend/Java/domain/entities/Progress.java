package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "progresses")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE progresses SET deleted = true where progress_id = ?")
@Data
public class Progress extends BaseEntity {
    @Column(name = "progress_number")
    private Integer progress;

    private boolean isCompleted = false;

    @OneToOne
    @MapsId
    private Enroll enroll;
}
