package com.example.tp_integrador.dtos.user;

import com.example.tp_integrador.entities.User;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Boolean deleted,
        String role
) {
    public static UserDto toDto(User user){
        if (user == null) return null;
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getDeleted(),
                user.getRole().toString()
        );
    }
}
