package com.example.tp_integrador.services;

import com.example.tp_integrador.dtos.category.CategoryCreate;
import com.example.tp_integrador.dtos.category.CategoryDto;
import com.example.tp_integrador.dtos.category.CategoryEdit;

import java.util.List;

public interface CategoryService {
    public CategoryDto save(CategoryCreate categoryCreate);
    public CategoryDto findById(Long id);
    public List <CategoryDto> findAll();
    public CategoryDto update (CategoryEdit categoryEdit, Long idCategoria);
    public void deleteById(Long id);
}
