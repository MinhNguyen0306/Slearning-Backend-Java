package com.example.Slearning.Backend.Java.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE categories SET deleted = true WHERE id = ?")
@Data
@AllArgsConstructor @NoArgsConstructor
public class Category extends BaseEntity{
    private String title;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SubCategory> subCategories = new HashSet<>();
}
