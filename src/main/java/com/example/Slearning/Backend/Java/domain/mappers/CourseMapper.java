package com.example.Slearning.Backend.Java.domain.mappers;

import com.example.Slearning.Backend.Java.domain.dtos.CourseDto;
import com.example.Slearning.Backend.Java.domain.entities.Course;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Course dtoToCourse(CourseDto courseDto) {
        return this.modelMapper.map(courseDto, Course.class);
    }

    public CourseDto courseToDto(Course course) {
        return this.modelMapper.map(course, CourseDto.class);
    }

    public List<CourseDto> coursesToDtos(List<Course> courses) {
        return courses.stream()
                .map(course -> this.modelMapper.map(course, CourseDto.class)).collect(Collectors.toList());
    }

    public List<Course> dtosToCourses(List<CourseDto> courseDtoList) {
        return courseDtoList.stream()
                .map(courseDto -> this.modelMapper.map(courseDto, Course.class)).collect(Collectors.toList());
    }
}
