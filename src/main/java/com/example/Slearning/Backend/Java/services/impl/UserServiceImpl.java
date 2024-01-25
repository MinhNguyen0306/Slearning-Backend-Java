package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.configs.AppConstants;
import com.example.Slearning.Backend.Java.domain.dtos.UserDto;
import com.example.Slearning.Backend.Java.domain.entities.*;
import com.example.Slearning.Backend.Java.domain.mappers.UserMapper;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import com.example.Slearning.Backend.Java.domain.responses.UserEnrollsResponse;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.exceptions.UploadFileException;
import com.example.Slearning.Backend.Java.repositories.ImageStorageRepository;
import com.example.Slearning.Backend.Java.repositories.RoleRepository;
import com.example.Slearning.Backend.Java.repositories.UserRepository;
import com.example.Slearning.Backend.Java.repositories.WorkExperienceRepository;
import com.example.Slearning.Backend.Java.services.FileStorageService;
import com.example.Slearning.Backend.Java.services.UserService;
import com.example.Slearning.Backend.Java.utils.FileUtils;
import com.example.Slearning.Backend.Java.utils.PageUtils;
import com.example.Slearning.Backend.Java.utils.enums.AdminFetchUserState;
import com.example.Slearning.Backend.Java.utils.enums.ResolveStatus;
import com.example.Slearning.Backend.Java.utils.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Value("${project.images}")
    private String imagePath;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ImageStorageRepository imageStorageRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private WorkExperienceRepository workExperienceRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Email", username));
        return user;
    }

    @Override
    public PageResponse<UserEnrollsResponse> getUserEnrollsOfMentor(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            UUID mentorId
    ) {
        User mentor = this.userRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor", "Id", mentorId));
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<User> page = userRepository.getUserEnrollsOfMentor(pageable, mentorId);

        List<User> users = page.getContent();
        List<UserDto> userDtos = this.userMapper.usersToDtos(users);
        List<UserEnrollsResponse> content = new ArrayList<>();

        for(UserDto userDto : userDtos) {
            UserEnrollsResponse userEnrollsResponse = new UserEnrollsResponse();
            List<String> coursesName = new ArrayList<>();
            for(Payment payment: userDto.getPayments()) {
                Course course = payment.getCourse();
                if(course.getUser().getId().equals(mentor.getId())) {
                    coursesName.add(course.getTitle());
                }
            }
            userEnrollsResponse.setUser(userDto);
            userEnrollsResponse.setCoursesName(coursesName);
            content.add(userEnrollsResponse);
        }

        PageResponse<UserEnrollsResponse> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(content);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public UserDto getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        return userMapper.userToDto(user);
    }

    @Override
    public UserDto lockUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        user.setLock(true);
        User updatedUser = this.userRepository.save(user);
        return this.userMapper.userToDto(updatedUser);
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
    public UserDto updateUserAbout(UUID userId, String about) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        user.setAbout(about);
        User updatedUser = userRepository.save(user);
        return userMapper.userToDto(updatedUser);
    }

    @Override
    public UserDto updateWorkExperience(UUID userId, WorkExperience workExperience) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        workExperience.setUser(user);
        WorkExperience createdWorkExperience = this.workExperienceRepository.save(workExperience);
        user.addWorkExperience(createdWorkExperience);
        User updatedUser = this.userRepository.save(user);
        return this.userMapper.userToDto(updatedUser);
    }

    @Override
    public UserDto updateAvatar(UUID userId, MultipartFile avatar) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        ImageStorage prevAvatar = user.getAvatar();
        String orgName = avatar.getOriginalFilename();
        String extension = FileUtils.getExtensionFile(orgName);
        String name = FileUtils.getFileName(orgName);
        try {
            String fileNameUploaded = this.fileStorageService.uploadFile(imagePath, avatar);
            ImageStorage imageStorage = new ImageStorage();
            imageStorage.setUser(user);
            imageStorage.setSize(avatar.getSize());
            imageStorage.setExtension(extension);
            imageStorage.setName(name);
            imageStorage.setUrl(fileNameUploaded);
            ImageStorage createdAvatar = this.imageStorageRepository.save(imageStorage);
            user.setAvatar(createdAvatar);
            User updatedUser = this.userRepository.save(user);
            if(createdAvatar != null && updatedUser != null) {
                if(prevAvatar != null) {
                    this.imageStorageRepository.delete(prevAvatar);
                }
                return this.userMapper.userToDto(updatedUser);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new UploadFileException(orgName);
        }
    }

    @Override
    public ApiResponse registerInstructor(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        if(user.isInstructor()) {
            return ApiResponse
                    .builder()
                    .message("Account has been Instructor")
                    .status("failed")
                    .build();
        }
        if(user.getAbout() == "" || user.getWorkExperiences().size() == 0 || user.getAvatar() == null) {
            return ApiResponse
                    .builder()
                    .message("Not enough requirement to register")
                    .status("failed")
                    .build();
        } else {
            if(user.getUserStatus() != UserStatus.ACTIVE && user.getUserStatus() != null) {
                if(user.isLock()) {
                    return ApiResponse
                            .builder()
                            .message("User locked")
                            .status("failed")
                            .build();
                }
                return ApiResponse
                        .builder()
                        .message("Request has sent")
                        .status("failed")
                        .build();
            } else {
                user.setUserStatus(UserStatus.PENDING);
                User updatedUser = this.userRepository.save(user);
                if(updatedUser != null) {
                    return ApiResponse
                            .builder()
                            .message("Request has sent")
                            .status("success")
                            .build();
                } else {
                    return ApiResponse
                            .builder()
                            .message("Server Error")
                            .status("failed")
                            .build();
                }
            }
        }
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

        List<User> users = page.getContent();
        List<UserDto> content = this.userMapper.usersToDtos(users);

        PageResponse<UserDto> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(content);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public PageResponse<UserDto> getUsersByAdminFetchState(
        Integer pageNumber,
        Integer pageSize,
        String sortBy,
        String sortDir,
        AdminFetchUserState adminFetchUserState
    ) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<User> page = null;
        if(adminFetchUserState.equals(AdminFetchUserState.INSTRUCTOR)) {
            page = this.userRepository.filterUserIsInstructor(pageable);
        } else {
            UserStatus userStatus = UserStatus.ACTIVE;
            switch (adminFetchUserState) {
                case PENDING -> userStatus = UserStatus.PENDING;
                case ACTIVE -> userStatus = UserStatus.ACTIVE;
                case LOCK -> userStatus = UserStatus.LOCK;
            }

            page = this.userRepository.filterUserByStatus(pageable, userStatus);
        }

        List<User> users = page.getContent().stream()
                .filter(user -> user.getRoles().stream().allMatch(role -> !role.getRole().equals(AppConstants.ROLE_ADMIN)))
                .collect(Collectors.toList());
        List<UserDto> content = this.userMapper.usersToDtos(users);

        PageResponse<UserDto> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(content);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public PageResponse<UserDto> filterUsersByStatus(
        Integer pageNumber,
        Integer pageSize,
        String sortBy,
        String sortDir,
        UserStatus userStatus
    ) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<User> page = this.userRepository.filterUserByStatus(pageable, userStatus);

        List<User> users = page.getContent();
        List<UserDto> content = this.userMapper.usersToDtos(users);

        PageResponse<UserDto> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setContent(content);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public ApiResponse resolveRegisterInstructor(UUID userId, ResolveStatus resolveStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        if(user.isInstructor() || user.isLock() || !user.getUserStatus().equals(UserStatus.PENDING)) {
            return ApiResponse.builder()
                    .message("Yêu cầu không hợp lệ")
                    .status("valid").build();
        }

        if(resolveStatus.equals((ResolveStatus.REJECT))) {
            user.setUserStatus(UserStatus.ACTIVE);
            User updatedUser = this.userRepository.save(user);
            if(updatedUser == null) {
                return ApiResponse.builder()
                        .message("Lỗi server")
                        .status("error").build();
            }
            return ApiResponse.builder()
                    .message("Đã từ chối yêu cầu")
                    .status("rejected").build();
        }

        Role role = this.roleRepository.findRoleByName(AppConstants.ROLE_MENTOR)
                        .orElseThrow(() -> new ResourceNotFoundException("Role", "Name", AppConstants.ROLE_MENTOR));

        user.setUserStatus(UserStatus.ACTIVE);
        user.setInstructor(true);
        user.addRole(role);
        User updatedUser = this.userRepository.save(user);
        if(updatedUser != null) {
            return ApiResponse.builder()
                    .message("Đã duyệt yêu cầu")
                    .status("accepted").build();
        }

        return ApiResponse.builder()
                .message("Lỗi server")
                .status("error").build();
    }
}
