package com.foodbridge.dto;
import jakarta.validation.constraints.*;
public record RegisterRequest(@NotBlank String name,@Email @NotBlank String email,@Size(min=6) String password,
 @NotNull String role,String phone,String organization,String address) {}
