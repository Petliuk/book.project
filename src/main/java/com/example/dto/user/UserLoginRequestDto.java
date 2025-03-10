package com.example.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserLoginRequestDto {
    @NotBlank
    @Length(min = 8, max = 20)
    @Email
    private String email;
    @NotBlank
    @Length(min = 8, max = 20)
    private String password;
}
