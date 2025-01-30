package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.UserRegistrationRequestDto;
import com.example.dto.UserResponseDto;
import com.example.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toEntity(UserRegistrationRequestDto requestDto);

    UserResponseDto toDto(User user);
}
