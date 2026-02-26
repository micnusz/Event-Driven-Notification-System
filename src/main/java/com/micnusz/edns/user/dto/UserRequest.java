package com.micnusz.edns.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest (@NotBlank(message = "Email is required")
                           @Email(message = "Invalid email format")
                           @Size(max = 255, message = "Email cannot exceed 255 characters")
                           String email
) { }