package com.example.Slearning.Backend.Java.domain.mappers;

import com.example.Slearning.Backend.Java.domain.dtos.UserDto;
import com.example.Slearning.Backend.Java.domain.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    @Autowired
    private ModelMapper modelMapper;

    public UserDto userToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public User dtoToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public List<User> dtosToUsers(List<UserDto> userDtos) {
        return userDtos.stream()
                .map(userDto -> this.modelMapper.map(userDto, User.class)).collect(Collectors.toList());
    }

    public List<UserDto> usersToDtos(List<User> users) {
        return users.stream()
                .map(user -> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }
}
