package com.example.tp_integrador.dtos.category;

import com.example.tp_integrador.entities.Category;

public record CategoryEdit(
        String name,
        String description
) {
    public void applyTo(Category category){
        if(this.name != null){
            category.setName(name);
        }
        if (this.description != null){
            category.setDescription(description);
        }
    }
}
