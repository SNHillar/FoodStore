package com.example.tp_integrador.services.impl;

import com.example.tp_integrador.dtos.category.CategoryCreate;
import com.example.tp_integrador.dtos.category.CategoryDto;
import com.example.tp_integrador.dtos.category.CategoryEdit;
import com.example.tp_integrador.entities.Category;
import com.example.tp_integrador.repositories.CategoryRepository;
import com.example.tp_integrador.services.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto save(CategoryCreate categoryCreate) {
        Category category = categoryCreate.toEntity();
        category = categoryRepository.save(category);
        return CategoryDto.toDto(category);
    }

    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow (() -> new IllegalArgumentException("Category not found with id: " + id));
        return CategoryDto.toDto(category);
    }

    @Override
    public List<CategoryDto> findAll() {
        List <Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryDto::toDto).toList();
    }

    @Override
    public CategoryDto update(CategoryEdit categoryEdit, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow( () -> new IllegalArgumentException("Category not found with id: " + categoryId));
        categoryEdit.applyTo(category);
        category = categoryRepository.save(category);
        return CategoryDto.toDto(category);
    }

    @Override
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
        category.setDeleted(true);
        categoryRepository.save(category);
    }
}
