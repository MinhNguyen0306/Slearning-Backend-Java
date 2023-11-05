package com.example.Slearning.Backend.Java.domain.mappers;

import com.example.Slearning.Backend.Java.domain.dtos.CategoryDto;
import com.example.Slearning.Backend.Java.domain.entities.Category;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Category dtoToCategory(CategoryDto categoryDto) {
        return this.modelMapper.map(categoryDto, Category.class);
    }

    public CategoryDto categoryToDto(Category category) {
        return this.modelMapper.map(category, CategoryDto.class);
    }

    public List<Category> dtosToCategories(List<CategoryDto> categoryDtoList) {
        return categoryDtoList.stream()
                .map(dto -> this.modelMapper.map(dto, Category.class)).collect(Collectors.toList());
    }

    public List<CategoryDto> categoriesToDtos(List<Category> categories) {
        return categories.stream()
                .map(category -> this.modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());
    }
}
