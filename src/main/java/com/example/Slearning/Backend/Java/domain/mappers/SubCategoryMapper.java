package com.example.Slearning.Backend.Java.domain.mappers;

import com.example.Slearning.Backend.Java.domain.dtos.SubCategoryDto;
import com.example.Slearning.Backend.Java.domain.entities.SubCategory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubCategoryMapper {

    @Autowired
    private ModelMapper modelMapper;

    public SubCategoryDto subCategoryToDto(SubCategory subCategory) {
        return this.modelMapper.map(subCategory, SubCategoryDto.class);
    }

    public SubCategory dtoToSubCategory(SubCategoryDto subCategoryDto) {
        return this.modelMapper.map(subCategoryDto, SubCategory.class);
    }

    public List<SubCategory> dtosToSubCategories(List<SubCategoryDto> subCategoryDtoList) {
        return subCategoryDtoList.stream()
                .map(dto -> this.modelMapper.map(dto, SubCategory.class)).collect(Collectors.toList());
    }

    public List<SubCategoryDto> subCategoriesToDtos(List<SubCategory> subCategories) {
        return subCategories.stream()
                .map(subCategory -> this.modelMapper.map(subCategory, SubCategoryDto.class)).collect(Collectors.toList());
    }
}
