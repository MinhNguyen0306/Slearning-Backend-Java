package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Set;

@Entity
@Table(name = "levels")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE levels SET deleted = true where level_id = ?")
@Data
@NoArgsConstructor
public class Level extends BaseEntity {
    @Column(name = "level_title", nullable = false, unique = true)
    private String title;

    @ManyToMany(mappedBy = "levels")
    private Set<Course> course;
}
