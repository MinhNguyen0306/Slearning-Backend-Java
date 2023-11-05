package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.domain.dtos.CategoryDto;
import com.example.Slearning.Backend.Java.domain.dtos.SubCategoryDto;
import com.example.Slearning.Backend.Java.domain.dtos.TopicDto;
import com.example.Slearning.Backend.Java.domain.entities.Topic;
import com.example.Slearning.Backend.Java.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categoryDtoList = categoryService.getAllCategories();
        if(categoryDtoList.isEmpty() || categoryDtoList == null) {
            return new ResponseEntity<>(categoryDtoList, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categoryDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id")UUID cateId) {
        CategoryDto categoryDto = categoryService.getCategoryById(cateId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        if(createdCategory == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @RequestBody @Valid CategoryDto categoryDto,
            @PathVariable("id") UUID cateId
    ) {
        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto, cateId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") UUID cateId) {
        categoryService.deleteCategory(cateId);
        return new ResponseEntity<>("Delete Category Successfully!", HttpStatus.OK);
    }

    @PostMapping("/subCategories")
    public ResponseEntity<SubCategoryDto> createSubCategory(@RequestBody @Valid SubCategoryDto subCategoryDto) {
        SubCategoryDto createdSubCategory = categoryService.createSubCategory(subCategoryDto);
        if(createdSubCategory == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(createdSubCategory, HttpStatus.CREATED);
    }

    @PutMapping("/subCategories/{id}")
    public ResponseEntity<SubCategoryDto> updateSubCategory(
            @RequestBody @Valid SubCategoryDto subCategoryDto,
            @PathVariable("id") UUID subId
    ) {
        SubCategoryDto updatedSubCategory = categoryService.updateSubCategory(subCategoryDto, subId);
        return new ResponseEntity<>(updatedSubCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/subCategories/{id}")
    public ResponseEntity<String> deleteSubCategory(@PathVariable("id") UUID subId) {
        categoryService.deleteSubCategory(subId);
        return new ResponseEntity<>("Delete SubCategory Successfully!", HttpStatus.OK);
    }

    @PostMapping("/topics/create")
    public ResponseEntity<TopicDto> createTopic(@RequestBody @Valid TopicDto topicDto) {
        TopicDto createdTopic = categoryService.createTopic(topicDto);
        if(createdTopic == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(createdTopic, HttpStatus.CREATED);
    }

    @PutMapping("/topics/{id}")
    public ResponseEntity<TopicDto> updateTopic(
            @RequestBody @Valid TopicDto topicDto,
            @PathVariable("id") UUID topicId
    ) {
        TopicDto updatedTopic = categoryService.updateTopic(topicDto, topicId);
        return new ResponseEntity<>(updatedTopic, HttpStatus.CREATED);
    }

    @DeleteMapping("/topics/{id}")
    public ResponseEntity<String> deleteTopic(@PathVariable("id") UUID topicId) {
        categoryService.deleteTopic(topicId);
        return new ResponseEntity<>("Delete Topic Successfully!", HttpStatus.OK);
    }
}
