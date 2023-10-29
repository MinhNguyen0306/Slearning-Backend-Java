package com.example.Slearning.Backend.Java.domain.mappers;

import com.example.Slearning.Backend.Java.domain.dtos.UserDto;
import com.example.Slearning.Backend.Java.domain.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
