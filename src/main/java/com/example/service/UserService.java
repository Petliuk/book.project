package com.example.service;

import com.example.dto.user.UserRegistrationRequestDto;
import com.example.dto.user.UserResponseDto;
import com.example.model.User;
import org.springframework.security.core.Authentication;

public interface UserService {

    UserResponseDto register(UserRegistrationRequestDto requestDto);

    User getAuthenticatedUser(Authentication authentication);
}
