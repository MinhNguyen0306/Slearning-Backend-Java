package com.example.Slearning.Backend.Java.domain.mappers;

import com.example.Slearning.Backend.Java.domain.dtos.TopicDto;
import com.example.Slearning.Backend.Java.domain.entities.Topic;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TopicMapper {

    @Autowired
    private ModelMapper modelMapper;

    public TopicDto topicToDto(Topic topic) {
        return this.modelMapper.map(topic, TopicDto.class);
    }

    public Topic dtoToTopic(TopicDto topicDto) {
        return this.modelMapper.map(topicDto, Topic.class);
    }

    public List<Topic> dtosToTopics(List<TopicDto> topicDtoList) {
        return topicDtoList.stream()
                .map(topicDto -> this.dtoToTopic(topicDto)).collect(Collectors.toList());
    }

    public List<TopicDto> topicsToDtos(List<Topic> topics) {
        return topics.stream()
                .map(topic -> this.topicToDto(topic)).collect(Collectors.toList());
    }
}
