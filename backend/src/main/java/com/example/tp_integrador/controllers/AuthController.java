package com.example.tp_integrador.controllers;

import com.example.tp_integrador.dtos.auth.login.LoginRequestDTO;
import com.example.tp_integrador.dtos.auth.register.RegisterRequestDTO;
import com.example.tp_integrador.dtos.user.UserDto;
import com.example.tp_integrador.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity <UserDto> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        UserDto userDto = authService.register(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        UserDto userDto = authService.login(loginRequestDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userDto);
    }
}
