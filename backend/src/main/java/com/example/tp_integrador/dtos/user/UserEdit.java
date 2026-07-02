package com.example.tp_integrador.dtos.user;

import com.example.tp_integrador.entities.User;
import com.example.tp_integrador.enums.Rol;

public record UserEdit(
        String firstName,
        String lastName,
        String email,
        String phone,
        String password,
        String role
) {
    public void applyTo(User user){
        if(firstName != null){
            user.setFirstName(firstName);
        }
        if (lastName != null) {
            user.setLastName(lastName);
        }
        if (email != null) {
            user.setEmail(email);
        }
        if (phone != null) {
            user.setPhone(phone);
        }
        if (password != null) {
            user.setPassword(password);
        }
        if (role != null) {
            user.setRole(Rol.valueOf(role.trim().toUpperCase()));
        }
    }
}
