package com.example.tp_integrador.services;


import com.example.tp_integrador.dtos.auth.login.LoginRequestDTO;
import com.example.tp_integrador.dtos.auth.register.RegisterRequestDTO;
import com.example.tp_integrador.dtos.user.UserDto;

public interface AuthService {
    public UserDto login(LoginRequestDTO  loginRequestDTO);
    public UserDto register(RegisterRequestDTO registerRequestDTO);
}
