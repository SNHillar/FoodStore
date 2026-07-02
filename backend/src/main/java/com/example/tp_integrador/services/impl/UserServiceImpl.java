package com.example.tp_integrador.services.impl;

import com.example.tp_integrador.dtos.user.UserCreate;
import com.example.tp_integrador.dtos.user.UserDto;
import com.example.tp_integrador.dtos.user.UserEdit;
import com.example.tp_integrador.entities.User;
import com.example.tp_integrador.repositories.UserRepository;
import com.example.tp_integrador.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDto save(UserCreate userCreate) {
        User user = userCreate.toEntity();
        userRepository.save(user);
        return UserDto.toDto(user);
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return UserDto.toDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDto::toDto).toList();
    }

    @Override
    public UserDto update(UserEdit userEdit, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        userEdit.applyTo(user);
        userRepository.save(user);
        return UserDto.toDto(user);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        return UserDto.toDto(user);
    }
}
