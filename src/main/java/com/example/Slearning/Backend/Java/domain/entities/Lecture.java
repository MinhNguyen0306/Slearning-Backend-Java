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

    @Column(name = "lecture_video", columnDefinition = "LONGBLOB")
    @Lob
    private Blob video;

    @Column(name = "lecture_position")
    private Integer position;

    @Column(name = "lecture_file_attach")
    @Lob
    private byte[] fileAttach;

    private boolean isPreviewed = false;

    private boolean isLast;

    @Enumerated(EnumType.STRING)
    private PublishStatus publishStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
}
