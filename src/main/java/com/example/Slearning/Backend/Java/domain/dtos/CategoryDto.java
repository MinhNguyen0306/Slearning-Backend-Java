package com.example.Slearning.Backend.Java.domain.dtos;

import com.example.Slearning.Backend.Java.domain.entities.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor @AllArgsConstructor
public class CategoryDto {
    private UUID id;
    private String title;
    private boolean isLock;
    private Set<SubCategory> subCategories;
}
