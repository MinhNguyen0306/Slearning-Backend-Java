package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.configs.AppConstants;
import com.example.Slearning.Backend.Java.domain.dtos.CategoryDto;
import com.example.Slearning.Backend.Java.domain.dtos.LevelDto;
import com.example.Slearning.Backend.Java.domain.dtos.SubCategoryDto;
import com.example.Slearning.Backend.Java.domain.dtos.TopicDto;
import com.example.Slearning.Backend.Java.domain.entities.Level;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import com.example.Slearning.Backend.Java.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<PageResponse<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ) {
        PageResponse<CategoryDto> pageResponse = this.categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/subCategories")
    public ResponseEntity<PageResponse<SubCategoryDto>> getAllSubCategories(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ) {
        PageResponse<SubCategoryDto> pageResponse = this.categoryService.getAllSubCategories(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/{categoryId}/subCategories")
    public ResponseEntity<PageResponse<SubCategoryDto>> getAllSubCategoriesOfCategory(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
            @PathVariable UUID categoryId
    ) {
        PageResponse<SubCategoryDto> pageResponse = this.categoryService.getAllSubCategoriesOfCategory(
                pageNumber, pageSize, sortBy, sortDir, categoryId
        );
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/topics")
    public ResponseEntity<PageResponse<TopicDto>> getAllTopics(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ) {
        PageResponse<TopicDto> pageResponse = this.categoryService.getAllTopics(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/{subCategoryId}/topics")
    public ResponseEntity<PageResponse<TopicDto>> getAllTopicsOfSubCategory(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
            @PathVariable UUID subCategoryId
    ) {
        PageResponse<TopicDto> pageResponse = this.categoryService.getAllTopicsOfSubCategory(
                pageNumber, pageSize, sortBy, sortDir, subCategoryId
        );
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") UUID cateId) {
        CategoryDto categoryDto = categoryService.getCategoryById(cateId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@RequestParam("title") String title ) {
        CategoryDto createdCategory = this.categoryService.createCategory(title);
        if(createdCategory == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @RequestParam("title") String title,
            @PathVariable("id") UUID cateId
    ) {
        CategoryDto updatedCategory = categoryService.updateCategory(title, cateId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") UUID cateId) {
        categoryService.deleteCategory(cateId);
        return new ResponseEntity<>("Delete Category Successfully!", HttpStatus.OK);
    }

    @PostMapping("/subCategories")
    public ResponseEntity<SubCategoryDto> createSubCategory(
            @RequestParam("title") String title,
            @RequestParam("categoryId") UUID categoryId
    ) {
        SubCategoryDto createdSubCategory = categoryService.createSubCategory(title, categoryId);
        if(createdSubCategory == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(createdSubCategory, HttpStatus.CREATED);
    }

    @PutMapping("/subCategories/{id}")
    public ResponseEntity<SubCategoryDto> updateSubCategory(
            @RequestParam("title") String title,
            @PathVariable("id") UUID subId
    ) {
        SubCategoryDto updatedSubCategory = categoryService.updateSubCategory(title, subId);
        return new ResponseEntity<>(updatedSubCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/subCategories/{id}")
    public ResponseEntity<String> deleteSubCategory(@PathVariable("id") UUID subId) {
        categoryService.deleteSubCategory(subId);
        return new ResponseEntity<>("Delete SubCategory Successfully!", HttpStatus.OK);
    }

    @PostMapping("/topics")
    public ResponseEntity<TopicDto> createTopic(
            @RequestParam("title") String title,
            @RequestParam("subCategoryId") UUID subCategoryId
    ) {
        TopicDto createdTopic = categoryService.createTopic(title, subCategoryId);
        if(createdTopic == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(createdTopic, HttpStatus.CREATED);
    }

    @PutMapping("/topics/{id}")
    public ResponseEntity<TopicDto> updateTopic(
            @RequestParam("title") String title,
            @PathVariable("id") UUID topicId
    ) {
        TopicDto updatedTopic = categoryService.updateTopic(title, topicId);
        return new ResponseEntity<>(updatedTopic, HttpStatus.CREATED);
    }

    @DeleteMapping("/topics/{id}")
    public ResponseEntity<String> deleteTopic(@PathVariable("id") UUID topicId) {
        categoryService.deleteTopic(topicId);
        return new ResponseEntity<>("Delete Topic Successfully!", HttpStatus.OK);
    }

    @GetMapping("/levels")
    public ResponseEntity<List<Level>> gelAllLevels() {
        return ResponseEntity.ok(this.categoryService.gelAllLevels());
    }
}
