package com.example.tp_integrador.dtos.category;

import com.example.tp_integrador.entities.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreate(
        @NotBlank
        @Size(min = 3, max = 20, message = "Enter a valid name.")
        String name,
        @NotBlank
        @Size(min = 10, max = 1000, message = "Enter a valid description.")
        String description
) {
    public Category toEntity(){
        return new Category(this.name, this.description);
    }
}
