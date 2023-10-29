package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.dtos.UserDto;
import com.example.Slearning.Backend.Java.domain.entities.User;
import com.example.Slearning.Backend.Java.domain.mappers.UserMapper;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.repositories.UserRepository;
import com.example.Slearning.Backend.Java.services.UserService;
import com.example.Slearning.Backend.Java.utils.PageUtils;
import com.example.Slearning.Backend.Java.utils.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Email", username));
        return user;
    }

    @Override
    public UserDto getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        return userMapper.userToDto(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        userRepository.delete(user);
    }

    @Override
    public UserDto updateUserInformation(UserDto userDto, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        user.setAbout(userDto.getAbout());
        user.setAge(userDto.getAge());
        user.setEducation(userDto.getEducation());
        user.setFullName(userDto.getFullName());
        User updatedUser = userRepository.save(user);
        return userMapper.userToDto(updatedUser);
    }

    @Override
    public PageResponse<UserDto> getAllUsers(
        Integer pageNumber,
        Integer pageSize,
        String sortBy,
        String sortDir
    ) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<User> page = userRepository.findAll(pageable);
        PageResponse<UserDto> pageResponse = PageUtils.<User, UserDto>paging(page, pageNumber, pageSize);
        return pageResponse;
    }

    @Override
    public PageResponse<UserDto> filterUsersByStatus(UserStatus userStatus) {
        return null;
    }
}
