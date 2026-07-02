package com.example.tp_integrador.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString
public class Product extends Base{

    @EqualsAndHashCode.Include
    private String name;
    private Double price;
    private String description;
    private Integer stock;
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private Set<OrderDetail> orderDetail;

    // Constructor DTO.
    public Product(String name, Double price, String description, Integer stock, String image, Category category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.image = image;
        this.category = category;
    }
}
