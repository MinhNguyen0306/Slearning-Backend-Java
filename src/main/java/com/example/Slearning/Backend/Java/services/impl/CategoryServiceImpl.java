package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.dtos.CategoryDto;
import com.example.Slearning.Backend.Java.domain.dtos.SubCategoryDto;
import com.example.Slearning.Backend.Java.domain.dtos.TopicDto;
import com.example.Slearning.Backend.Java.domain.entities.Category;
import com.example.Slearning.Backend.Java.domain.entities.SubCategory;
import com.example.Slearning.Backend.Java.domain.entities.Topic;
import com.example.Slearning.Backend.Java.domain.mappers.CategoryMapper;
import com.example.Slearning.Backend.Java.domain.mappers.SubCategoryMapper;
import com.example.Slearning.Backend.Java.domain.mappers.TopicMapper;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.repositories.CategoryRepository;
import com.example.Slearning.Backend.Java.repositories.SubCategoryRepository;
import com.example.Slearning.Backend.Java.repositories.TopicRepository;
import com.example.Slearning.Backend.Java.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SubCategoryMapper subCategoryMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.categoriesToDtos(categories);
    }

    @Override
    public CategoryDto getCategoryById(UUID cateId) {
        Category category = categoryRepository.findById(cateId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", cateId));
        return categoryMapper.categoryToDto(category);
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.dtoToCategory(categoryDto);
        Category createdCategory = categoryRepository.save(category);
        return categoryMapper.categoryToDto(createdCategory);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, UUID cateId) {
        Category category = categoryRepository.findById(cateId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", cateId));
        category.setTitle(categoryDto.getTitle());
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.categoryToDto(updatedCategory);
    }

    @Override
    public void deleteCategory(UUID cateId) {
        Category category = categoryRepository.findById(cateId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", cateId));
        categoryRepository.delete(category);
    }

    @Override
    public SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto) {
        SubCategory subCategory = subCategoryMapper.dtoToSubCategory(subCategoryDto);
        SubCategory createdSubCategory = subCategoryRepository.save(subCategory);
        return subCategoryMapper.subCategoryToDto(createdSubCategory);
    }

    @Override
    public SubCategoryDto updateSubCategory(SubCategoryDto subCategoryDto, UUID subId) {
        SubCategory subCategory = subCategoryRepository.findById(subId)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "Id", subId));
        subCategory.setTitle(subCategoryDto.getTitle());
        SubCategory updatedSubCategory = subCategoryRepository.save(subCategory);
        return subCategoryMapper.subCategoryToDto(updatedSubCategory);
    }

    @Override
    public void deleteSubCategory(UUID subId) {
        SubCategory subCategory = subCategoryRepository.findById(subId)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "Id", subId));
        subCategoryRepository.delete(subCategory);
    }

    @Override
    public TopicDto createTopic(TopicDto topicDto) {
        Topic topic = topicMapper.dtoToTopic(topicDto);
        Topic createdTopic = topicRepository.save(topic);
        return topicMapper.topicToDto(createdTopic);
    }

    @Override
    public TopicDto updateTopic(TopicDto topicDto, UUID topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "Id", topicId));
        topic.setTitle(topicDto.getTitle());
        Topic updatedTopic = topicRepository.save(topic);
        return topicMapper.topicToDto(updatedTopic);
    }

    @Override
    public void deleteTopic(UUID topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "Id", topicId));
        topicRepository.delete(topic);
    }
}
