package com.example.Slearning.Backend.Java.domain.mappers;

import com.example.Slearning.Backend.Java.domain.dtos.ChapterDto;
import com.example.Slearning.Backend.Java.domain.entities.Chapter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChapterMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Chapter dtoToChapter(ChapterDto chapterDto) {
        return this.modelMapper.map(chapterDto, Chapter.class);
    }

    public ChapterDto chapterToDto(Chapter chapter) {
        return this.modelMapper.map(chapter, ChapterDto.class);
    }

    public List<Chapter> dtosToChapters(List<ChapterDto> chapterDtoList) {
        return chapterDtoList.stream()
                .map(chapterDto -> this.modelMapper.map(chapterDto, Chapter.class)).collect(Collectors.toList());
    }

    public List<ChapterDto> chaptersToDtos(List<Chapter> chapters) {
        return chapters.stream()
                .map(chapter -> this.modelMapper.map(chapter, ChapterDto.class)).collect(Collectors.toList());
    }
}
