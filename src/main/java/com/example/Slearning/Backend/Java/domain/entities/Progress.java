package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "progresses")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE progresses SET deleted = true where progress_id = ?")
@Data
public class Progress extends BaseEntity implements Comparable<Progress> {

    @ManyToOne
    @JoinColumn(name = "lecture_id", referencedColumnName = "id")
    private Lecture lecture;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "user_completed_progress")
    private boolean isCompleted = false;

    @Override
    public int compareTo(Progress progress) {
        if(this.lecture.getPosition() > progress.lecture.getPosition()) {
            return 1;
        } else if(this.lecture.getPosition() < progress.getLecture().getPosition()) {
            return -1;
        } else {
            return 0;
        }
    }
}
