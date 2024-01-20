package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.dtos.UserDto;
import com.example.Slearning.Backend.Java.domain.entities.User;
import com.example.Slearning.Backend.Java.domain.entities.WorkExperience;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import com.example.Slearning.Backend.Java.utils.enums.AdminFetchUserState;
import com.example.Slearning.Backend.Java.utils.enums.ResolveStatus;
import com.example.Slearning.Backend.Java.utils.enums.UserStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService extends UserDetailsService {

    UserDto getUserById(UUID userId);

    UserDto updateUserInformation(UserDto userDto, UUID userId);

    UserDto updateUserAbout(UUID userId, String about);

    UserDto updateWorkExperience(UUID userId, WorkExperience workExperience);

    UserDto updateAvatar(UUID userId, MultipartFile avatar);

    ApiResponse registerInstructor(UUID userId);

    UserDto lockUser(UUID userId);

    PageResponse<UserDto> getAllUsers(
        Integer pageNumber,
        Integer pageSize,
        String sortBy,
        String sortDir
    );

    PageResponse<UserDto> getUsersByAdminFetchState(
        Integer pageNumber,
        Integer pageSize,
        String sortBy,
        String sortDir,
        AdminFetchUserState adminFetchUserState
    );

    PageResponse<UserDto> filterUsersByStatus(

        Integer pageNumber,
        Integer pageSize,
        String sortBy,
        String sortDir,
        UserStatus userStatus
    );

    ApiResponse resolveRegisterInstructor(UUID userId, ResolveStatus resolveStatus);

}
