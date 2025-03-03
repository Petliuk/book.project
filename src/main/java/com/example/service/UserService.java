package com.example.service;

import com.example.dto.user.UserRegistrationRequestDto;
import com.example.dto.user.UserResponseDto;

public interface UserService {

    UserResponseDto register(UserRegistrationRequestDto requestDto);

}
