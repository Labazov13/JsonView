package com.example.JsonView.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;



public record UserDTO(@NotBlank(message = "Field not can be empty") String name,
                      @Email(message = "Invalid email") String email) {
}
