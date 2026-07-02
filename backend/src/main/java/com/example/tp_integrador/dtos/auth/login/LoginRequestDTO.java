package com.example.tp_integrador.dtos.auth.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(

        @NotBlank(message = "El email no puede estar vacio.")
        @Email(message = "Formato de email invalido.")
        String email,

        @NotBlank
        @Size(min = 6, message = "La clave debe contener al menos 6 digitos.")
        String password
){}
