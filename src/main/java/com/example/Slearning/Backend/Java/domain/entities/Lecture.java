package com.example.Slearning.Backend.Java.domain.entities;

import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Blob;
import java.util.List;

@Entity
@Table(name = "lectures")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE lectures SET deleted = true WHERE lecture_id = ?")
@Data
@NoArgsConstructor @AllArgsConstructor
public class Lecture extends BaseEntity {

    @Column(name = "lecture_title")
    @Size(min = 2, max = 100, message = "Min 2 and max 100 characters")
    @NotEmpty(message = "Title not empty")
    private String title;

    @Column(name = "lecture_description")
    @Size(min = 20, max = 200, message = "Min 20 and Max 200 characters")
    private String description;

    @Column(name = "lecture_position")
    private Integer position;

    @Column(name = "is_previewed_lecture")
    private boolean isPreviewed = false;

    @Column(name = "is_last_lecture")
    private boolean isLast = false;

    @Column(name = "is_completed_lecture")
    private boolean isCompleted = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "lecture_publish_status")
    private PublishStatus publishStatus;

    @OneToOne
    @MapsId
    private VideoStorage videoStorage;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<LectureFileAttach> lectureFileAttaches;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
}
