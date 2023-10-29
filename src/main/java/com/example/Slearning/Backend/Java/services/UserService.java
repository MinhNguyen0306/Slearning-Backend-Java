package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.dtos.UserDto;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import com.example.Slearning.Backend.Java.utils.enums.UserStatus;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService extends UserDetailsService {
    UserDto getUserById(UUID userId);
    void deleteUser(UUID userId);
    UserDto updateUserInformation(UserDto userDto, UUID userId);
    PageResponse<UserDto> getAllUsers( Integer pageNumber,
                                       Integer pageSize,
                                       String sortBy,
                                       String sortDir);
    PageResponse<UserDto> filterUsersByStatus(UserStatus userStatus);
}
