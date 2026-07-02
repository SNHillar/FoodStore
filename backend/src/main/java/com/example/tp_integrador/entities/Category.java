package com.example.tp_integrador.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Category extends Base{

    @EqualsAndHashCode.Include
    private String name;

    private String description;

    @OneToMany(mappedBy = "category")
    private Set<Product> products;

    // CONSTRUCTOR FOR DTOs
    public Category(String name, String description){
        this.name = name;
        this.description = description;
    }
}
