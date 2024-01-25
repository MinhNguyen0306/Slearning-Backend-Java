package com.example.Slearning.Backend.Java.domain.entities;

import com.example.Slearning.Backend.Java.utils.enums.PublishStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lectures")
@Data
@NoArgsConstructor @AllArgsConstructor
public class Lecture extends BaseEntity implements Comparable<Lecture> {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "lecture_publish_status")
    private PublishStatus publishStatus;

    @OneToOne
    @JoinColumn(name = "video_id", referencedColumnName = "id")
    private VideoStorage videoStorage;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LectureFileAttach> lectureFileAttaches = new ArrayList<>();

    @OneToMany(mappedBy = "lecture")
    @JsonIgnore
    private List<Progress> progresses;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    @JsonIgnore
    private Chapter chapter;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CodingExercise> codingExercises;

    @Override
    public int compareTo(Lecture lecture) {
        if(this.getPosition() > lecture.getPosition()) {
            return 1;
        } else if(this.getPosition() < lecture.getPosition()) {
            return -1;
        } else {
            return 0;
        }
    }
}
