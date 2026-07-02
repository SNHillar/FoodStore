package com.example.tp_integrador.dtos.product;

import com.example.tp_integrador.entities.Category;
import com.example.tp_integrador.entities.Product;
import jakarta.validation.constraints.NotBlank;

public record ProductCreate(
        @NotBlank
        String name,
        Double price,
        String description,
        Integer stock,
        String image,
        Long categoryId
) {
    public Product toEntity(){
        return Product.builder()
                .name(this.name)
                .price(this.price)
                .description(this.description)
                .stock(this.stock)
                .image(this.image)
                .category(Category.builder()
                        .id(this.categoryId)
                        .build())
                .build();
    }
}
