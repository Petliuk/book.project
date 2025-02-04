package com.example.service;

import com.example.dto.UserRegistrationRequestDto;
import com.example.dto.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);
}
