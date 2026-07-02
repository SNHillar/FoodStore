package com.example.tp_integrador.controllers;


import com.example.tp_integrador.dtos.user.UserCreate;
import com.example.tp_integrador.dtos.user.UserDto;
import com.example.tp_integrador.dtos.user.UserEdit;
import com.example.tp_integrador.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserCreate userCreate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userCreate));
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<UserDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@Valid @RequestBody UserEdit userEdit, @PathVariable Long id){
        return ResponseEntity.ok(userService.update(userEdit, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
