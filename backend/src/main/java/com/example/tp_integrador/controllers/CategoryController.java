package com.example.tp_integrador.controllers;

import com.example.tp_integrador.dtos.category.CategoryCreate;
import com.example.tp_integrador.dtos.category.CategoryDto;
import com.example.tp_integrador.dtos.category.CategoryEdit;
import com.example.tp_integrador.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity <CategoryDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @GetMapping
    public ResponseEntity <List<CategoryDto>> findAll(){
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long id, @Valid @RequestBody CategoryEdit category){
        CategoryDto categoryDto = categoryService.update(category, id);
        return ResponseEntity.ok(categoryDto);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryCreate categoryCreate){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(categoryCreate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
