package com.example.tp_integrador.dtos.category;

import com.example.tp_integrador.entities.Category;


public record CategoryDto(
        Long id,
        String name,
        String description
) {
    public static CategoryDto toDto(Category category){
        return new CategoryDto(category.getId(), category.getName(), category.getDescription());
    }
}
