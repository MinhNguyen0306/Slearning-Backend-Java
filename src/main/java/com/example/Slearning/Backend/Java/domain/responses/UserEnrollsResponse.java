package com.example.Slearning.Backend.Java.domain.responses;

import com.example.Slearning.Backend.Java.domain.dtos.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserEnrollsResponse {
    private UserDto user;
    private List<String> coursesName;
}
