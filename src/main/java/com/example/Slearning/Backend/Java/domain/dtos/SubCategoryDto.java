package com.example.Slearning.Backend.Java.domain.dtos;

import com.example.Slearning.Backend.Java.domain.entities.Category;
import com.example.Slearning.Backend.Java.domain.entities.Topic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryDto {
    private UUID id;
    private String title;
    private boolean isLock;
    private Category category;
    private Set<Topic> topics;
}
