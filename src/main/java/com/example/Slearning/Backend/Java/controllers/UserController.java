package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.configs.AppConstants;
import com.example.Slearning.Backend.Java.domain.dtos.UserDto;
import com.example.Slearning.Backend.Java.domain.entities.WorkExperience;
import com.example.Slearning.Backend.Java.domain.responses.ApiResponse;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import com.example.Slearning.Backend.Java.services.UserService;
import com.example.Slearning.Backend.Java.utils.enums.AdminFetchUserState;
import com.example.Slearning.Backend.Java.utils.enums.ResolveStatus;
import com.example.Slearning.Backend.Java.utils.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<PageResponse<UserDto>> getAllUsers(
        @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
        @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ) {
        PageResponse<UserDto> pageResponse = this.userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID userId) {
        UserDto user = this.userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<UserDto> updateUserInformation(@RequestBody UserDto userDto, @PathVariable UUID userId) {
        UserDto updatedUser = this.userService.updateUserInformation(userDto, userId);
        if(updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/{userId}/about")
    public ResponseEntity<UserDto> updateUserAbout(@PathVariable UUID userId, @RequestParam("about") String about) {
        UserDto updatedUser = this.userService.updateUserAbout(userId, about);
        if(updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{userId}/work-experience")
    public ResponseEntity<UserDto> updateWorkExperience(@PathVariable UUID userId, @Valid @RequestBody WorkExperience workExperience) {
        UserDto updatedUser = this.userService.updateWorkExperience(userId, workExperience);
        if(updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{userId}/avatar")
    public ResponseEntity<UserDto> updateAvatar(@PathVariable UUID userId, @RequestPart(name = "avatar") MultipartFile avatar) {
        UserDto updatedUser = this.userService.updateAvatar(userId, avatar);
        if(updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/{userId}/register-instructor")
    public ResponseEntity<ApiResponse> registerInstructor(@PathVariable UUID userId) {
        ApiResponse apiResponse = this.userService.registerInstructor(userId);
        if(apiResponse.getStatus() == "failed") {
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/{userId}/lock")
    public ResponseEntity<UserDto> lockUser(@PathVariable UUID userId) {
        UserDto updatedUser = this.userService.lockUser(userId);
        if(updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/status")
    public ResponseEntity<PageResponse<UserDto>> filterUserByStatus(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
            @RequestParam(value = "status") UserStatus userStatus
    ) {
        PageResponse<UserDto> pageResponse = this.userService.filterUsersByStatus(pageNumber, pageSize, sortBy, sortDir, userStatus);
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/fetch-state")
    public ResponseEntity<PageResponse<UserDto>> getUsersByAdminFetchState(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
            @RequestParam(value = "state") AdminFetchUserState adminFetchUserState
    ) {
        PageResponse<UserDto> pageResponse = this.userService.getUsersByAdminFetchState(
                pageNumber, pageSize, sortBy, sortDir, adminFetchUserState
        );
        return ResponseEntity.ok(pageResponse);
    }

    @PatchMapping("/{userId}/register-instructor/resolve")
    public ResponseEntity<ApiResponse> resolveRegisterInstructor(
        @PathVariable UUID userId,
        @RequestParam("status") ResolveStatus resolveStatus
    ) {
        ApiResponse apiResponse = this.userService.resolveRegisterInstructor(userId, resolveStatus);
        if(apiResponse.getStatus() == "error") return ResponseEntity.internalServerError().body(apiResponse);
        else if(apiResponse.getStatus() == "valid") return ResponseEntity.badRequest().body(apiResponse);
        else if(apiResponse.getStatus() == "accepted") return ResponseEntity.ok().body(apiResponse);
        else return ResponseEntity.ok().body(apiResponse);
    }
}
