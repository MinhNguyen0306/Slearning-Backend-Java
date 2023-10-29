package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sub_category")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE t_user SET deleted = true WHERE sub_category_id = ?")
public class SubCategory extends BaseEntity{

    @Column(name = "sub_category_title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "subCategory", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Topic> topics = new HashSet<>();
}
