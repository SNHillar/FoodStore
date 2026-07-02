package com.example.tp_integrador.services;

import com.example.tp_integrador.dtos.user.UserCreate;
import com.example.tp_integrador.dtos.user.UserDto;
import com.example.tp_integrador.dtos.user.UserEdit;

import java.util.List;


public interface UserService {
    public UserDto save(UserCreate userCreate);
    public UserDto findById(Long id);
    public List<UserDto> findAll();
    public UserDto update(UserEdit userEdit, Long id);
    public void delete (Long id);
    public UserDto findByEmail(String email);
}
