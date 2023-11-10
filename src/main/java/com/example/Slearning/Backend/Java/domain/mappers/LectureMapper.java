package com.example.Slearning.Backend.Java.domain.mappers;

import com.example.Slearning.Backend.Java.domain.dtos.LectureDto;
import com.example.Slearning.Backend.Java.domain.entities.Lecture;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LectureMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Lecture dtoToLecture(LectureDto lectureDto) {
        return this.modelMapper.map(lectureDto, Lecture.class);
    }

    public LectureDto lectureToDto(Lecture lecture) {
        return this.modelMapper.map(lecture, LectureDto.class);
    }

    public List<Lecture> dtosToLectures(List<LectureDto> lectureDtoList) {
        return lectureDtoList.stream()
                .map(lectureDto -> this.modelMapper.map(lectureDto, Lecture.class)).collect(Collectors.toList());
    }

    public List<LectureDto> lecturesToDtos(List<Lecture> lectures) {
        return lectures.stream()
                .map(lecture -> this.modelMapper.map(lecture, LectureDto.class)).collect(Collectors.toList());
    }
}
