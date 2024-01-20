package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.dtos.*;
import com.example.Slearning.Backend.Java.domain.entities.*;
import com.example.Slearning.Backend.Java.domain.mappers.CategoryMapper;
import com.example.Slearning.Backend.Java.domain.mappers.SubCategoryMapper;
import com.example.Slearning.Backend.Java.domain.mappers.TopicMapper;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.repositories.CategoryRepository;
import com.example.Slearning.Backend.Java.repositories.LevelRepository;
import com.example.Slearning.Backend.Java.repositories.SubCategoryRepository;
import com.example.Slearning.Backend.Java.repositories.TopicRepository;
import com.example.Slearning.Backend.Java.services.CategoryService;
import com.example.Slearning.Backend.Java.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private LevelRepository levelRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SubCategoryMapper subCategoryMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Override
    public PageResponse<CategoryDto> getAllCategories(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir
    ) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<Category> page = this.categoryRepository.findAll(pageable);

        List<Category> categories = page.getContent();
        List<CategoryDto> content = this.categoryMapper.categoriesToDtos(categories);

        PageResponse<CategoryDto> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(content);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public PageResponse<SubCategoryDto> getAllSubCategories(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir
    ) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<SubCategory> page = this.subCategoryRepository.findAll(pageable);

        List<SubCategory> subCategories = page.getContent();
        List<SubCategoryDto> content = this.subCategoryMapper.subCategoriesToDtos(subCategories);

        PageResponse<SubCategoryDto> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(content);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public PageResponse<SubCategoryDto> getAllSubCategoriesOfCategory(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            UUID categoryId
    ) {
        this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId.toString()));
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<SubCategory> page = this.subCategoryRepository.findSubOfCategory(pageable, categoryId);

        List<SubCategory> subCategories = page.getContent();
        List<SubCategoryDto> content = this.subCategoryMapper.subCategoriesToDtos(subCategories);

        PageResponse<SubCategoryDto> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(content);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public PageResponse<TopicDto> getAllTopics(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<Topic> page = this.topicRepository.findAll(pageable);

        List<Topic> topics = page.getContent();
        List<TopicDto> content = this.topicMapper.topicsToDtos(topics);

        PageResponse<TopicDto> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(content);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public PageResponse<TopicDto> getAllTopicsOfSubCategory(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            UUID subCategoryId
    ) {
        this.subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "Id", subCategoryId.toString()));
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<Topic> page = this.topicRepository.findTopicOfSubCategory(pageable, subCategoryId);

        List<Topic> topics = page.getContent();
        List<TopicDto> content = this.topicMapper.topicsToDtos(topics);

        PageResponse<TopicDto> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(content);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public List<Level> gelAllLevels() {
        return this.levelRepository.findAll();
    }

    @Override
    public CategoryDto getCategoryById(UUID cateId) {
        Category category = categoryRepository.findById(cateId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", cateId));
        return categoryMapper.categoryToDto(category);
    }

    @Override
    public CategoryDto createCategory(String title) {
        Category category = new Category();
        category.setLock(false);
        category.setTitle(title);
        Category createdCategory = categoryRepository.save(category);
        return categoryMapper.categoryToDto(createdCategory);
    }

    @Override
    public CategoryDto updateCategory(String title, UUID cateId) {
        Category category = categoryRepository.findById(cateId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", cateId));
        category.setTitle(title);
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
    public SubCategoryDto createSubCategory(String title, UUID categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId));
        SubCategory subCategory = new SubCategory();
        subCategory.setTitle(title);
        subCategory.setCategory(category);
        subCategory.setLock(false);
        SubCategory createdSubCategory = subCategoryRepository.save(subCategory);
        return subCategoryMapper.subCategoryToDto(createdSubCategory);
    }

    @Override
    public SubCategoryDto updateSubCategory(String title, UUID subId) {
        SubCategory subCategory = subCategoryRepository.findById(subId)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "Id", subId));
        subCategory.setTitle(title);
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
    public TopicDto createTopic(String title, UUID subId) {
        SubCategory subCategory = this.subCategoryRepository.findById(subId)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "Id", subId));
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setSubCategory(subCategory);
        topic.setLock(false);
        Topic createdTopic = topicRepository.save(topic);
        return topicMapper.topicToDto(createdTopic);
    }

    @Override
    public TopicDto updateTopic(String title, UUID topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "Id", topicId));
        topic.setTitle(title);
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
