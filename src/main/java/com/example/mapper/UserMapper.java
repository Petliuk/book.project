package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.UserRegistrationRequestDto;
import com.example.dto.UserResponseDto;
import com.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toEntity(UserRegistrationRequestDto requestDto);

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "shippingAddress", target = "shippingAddress")
    UserResponseDto toDto(User user);
}
