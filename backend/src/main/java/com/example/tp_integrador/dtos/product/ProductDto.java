package com.example.tp_integrador.dtos.product;

import com.example.tp_integrador.dtos.category.CategoryDto;
import com.example.tp_integrador.entities.Product;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ProductDto(
        Long id,
        String name,
        Double price,
        String description,
        Integer stock,
        String image,
        Boolean deleted,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime created_at,
        CategoryDto categoryDto
) {
    public static ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getStock(),
                product.getImage(),
                product.getDeleted(),
                product.getCreatedAt(),
                product.getCategory() != null ? CategoryDto.toDto(product.getCategory()) : null
        );
    }
}