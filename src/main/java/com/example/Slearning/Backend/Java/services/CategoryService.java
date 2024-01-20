package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.dtos.CategoryDto;
import com.example.Slearning.Backend.Java.domain.dtos.LevelDto;
import com.example.Slearning.Backend.Java.domain.dtos.SubCategoryDto;
import com.example.Slearning.Backend.Java.domain.dtos.TopicDto;
import com.example.Slearning.Backend.Java.domain.entities.Level;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    PageResponse<CategoryDto> getAllCategories(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir
    );

    PageResponse<SubCategoryDto> getAllSubCategories(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir
    );

    PageResponse<SubCategoryDto> getAllSubCategoriesOfCategory(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            UUID categoryId
    );

    PageResponse<TopicDto> getAllTopics(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir
    );

    PageResponse<TopicDto> getAllTopicsOfSubCategory(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            UUID subCategoryId
    );

    List<Level> gelAllLevels();

    CategoryDto getCategoryById(UUID cateId);

    CategoryDto createCategory(String title);

    CategoryDto updateCategory(String title, UUID cateId);

    void deleteCategory(UUID cateId);

    SubCategoryDto createSubCategory(String title, UUID categoryId);

    SubCategoryDto updateSubCategory(String title, UUID subId);

    void deleteSubCategory(UUID subId);

    TopicDto createTopic(String title, UUID subId);

    TopicDto updateTopic(String title, UUID topicId);

    void deleteTopic(UUID topicId);
}
