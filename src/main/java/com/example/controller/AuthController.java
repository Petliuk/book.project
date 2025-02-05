package com.example.controller;

import com.example.dto.UserRegistrationRequestDto;
import com.example.dto.UserResponseDto;
import com.example.exception.RegistrationException;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/")
@Tag(name = "Authentication",
        description = "Endpoints for user authentication and registration")
public class AuthController {
    private final UserService userService;

    @Operation(summary = "Register a new user",
            description = "Registers a new user in the system and returns user details")
    @PostMapping("/registration")
    public UserResponseDto registerUser(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

}
