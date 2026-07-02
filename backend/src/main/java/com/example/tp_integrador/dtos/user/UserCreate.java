package com.example.tp_integrador.dtos.user;

import com.example.tp_integrador.entities.User;
import com.example.tp_integrador.enums.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record UserCreate(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank @Email
        String email,
        String phone,
        @NotBlank @Size (min = 6) @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$", message = "Password must contain at least one letter and one number")
        String password,
        @NotBlank
        String role
) {
    public User toEntity(){
        return User.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .phone(this.phone)
                .password(this.password)
                .role(this.role != null ? Rol.valueOf(this.role.trim().toUpperCase()) : null)
                .build();
    }
}
