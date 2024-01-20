package com.example.Slearning.Backend.Java.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sub_category")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE t_user SET deleted = true WHERE sub_category_id = ?")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategory extends BaseEntity{

    @Column(name = "sub_category_title")
    private String title;

    private boolean isLock = false;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Topic> topics;
}
