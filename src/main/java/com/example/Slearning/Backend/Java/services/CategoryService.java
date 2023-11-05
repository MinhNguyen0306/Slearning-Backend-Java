package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.dtos.CategoryDto;
import com.example.Slearning.Backend.Java.domain.dtos.SubCategoryDto;
import com.example.Slearning.Backend.Java.domain.dtos.TopicDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(UUID cateId);
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(CategoryDto categoryDto, UUID cateId);
    void deleteCategory(UUID cateId);

    SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto);
    SubCategoryDto updateSubCategory(SubCategoryDto subCategoryDto, UUID subId);
    void deleteSubCategory(UUID subId);

    TopicDto createTopic(TopicDto topicDto);
    TopicDto updateTopic(TopicDto topicDto, UUID topicId);
    void deleteTopic(UUID topicId);
}
