package com.example.tp_integrador.dtos.product;

import com.example.tp_integrador.entities.Category;
import com.example.tp_integrador.entities.Product;

public record ProductEdit(
        String name,
        Double price,
        String description,
        Integer stock,
        String image,
        Long categoryId
) {
    public void applyTo(Product product, Category category){
        if(this.name != null){
            product.setName(this.name);
        }
        if (this.price != null) {
            product.setPrice(this.price);
        }
        if (this.description != null) {
            product.setDescription(this.description);
        }
        if (this.stock != null) {
            product.setStock(this.stock);
        }
        if (this.image != null) {
            product.setImage(this.image);
        }
        if (category != null){
            product.setCategory(category);
        }
    }
}
