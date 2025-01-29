package com.example.service.impl;

import com.example.dto.UserRegistrationRequestDto;
import com.example.dto.UserResponseDto;
import com.example.exception.RegistrationException;
import com.example.mapper.UserMapper;
import com.example.model.User;
import com.example.repository.user.UserRepository;
import com.example.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (!requestDto.getPassword().equals(requestDto.getRepeatPassword())) {
            throw new RegistrationException("Passwords do not match");
        }
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Email is already taken");
        }

        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword((requestDto.getPassword()));
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setShippingAddress(requestDto.getShippingAddress());

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
