package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.validation.constraints.Min;

@Entity
@Table(name = "comments")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE comments SET deleted = true WHERE comment_id = ?")
public class Comment extends BaseEntity {

    @Column(name = "comment_content")
    @Min(value = 1, message = "At least 1 character")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
}
