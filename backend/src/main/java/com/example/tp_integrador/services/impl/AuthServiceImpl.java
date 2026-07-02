package com.example.tp_integrador.services.impl;

import com.example.tp_integrador.dtos.auth.login.LoginRequestDTO;
import com.example.tp_integrador.dtos.auth.register.RegisterRequestDTO;
import com.example.tp_integrador.dtos.user.UserDto;
import com.example.tp_integrador.entities.User;
import com.example.tp_integrador.enums.Rol;
import com.example.tp_integrador.repositories.UserRepository;
import com.example.tp_integrador.services.AuthService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDto login(LoginRequestDTO loginRequestDTO) {
        Optional<User> user= userRepository.findByEmail(loginRequestDTO.email());
        if(user.isEmpty() || user.get().getDeleted()){
            throw new IllegalArgumentException("User not found with email: " + loginRequestDTO.email());
        }
        if(!user.get().getPassword().equals(loginRequestDTO.password())){
            throw new IllegalArgumentException("Invalid password.");
        }
        return UserDto.toDto(user.get());
    }

    @Override
    public UserDto register(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.findByEmail(registerRequestDTO.email()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use." + registerRequestDTO.email());
        }
        User user = User.builder()
                .firstName(registerRequestDTO.firstName())
                .lastName(registerRequestDTO.lastName())
                .phone(registerRequestDTO.phone())
                .email(registerRequestDTO.email())
                .password(registerRequestDTO.password())
                .role(Rol.USER)
                .build();

        userRepository.save(user);
        return UserDto.toDto(user);
    }
}
