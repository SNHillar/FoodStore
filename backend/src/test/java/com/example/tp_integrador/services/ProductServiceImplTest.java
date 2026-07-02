package com.example.tp_integrador.services;

import com.example.tp_integrador.dtos.product.ProductCreate;
import com.example.tp_integrador.dtos.product.ProductDto;
import com.example.tp_integrador.dtos.product.ProductEdit;
import com.example.tp_integrador.entities.Category;
import com.example.tp_integrador.entities.Product;
import com.example.tp_integrador.repositories.ProductRepository;
import com.example.tp_integrador.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productoServiceImpl;

    Category category = Category.builder()
            .id(1L)
            .name("PIZZAS")
            .description("MASA MADRE")
            .build();


    private final Product PRODUCT_PREPARED = Product.builder()
            .id(1L)
            .name("Pizza")
            .price(1500.0)
            .description("Napolitana")
            .stock(10)
            .image("example.png")
            .category(category)
            .orderDetail(new HashSet<>())
            .build();

    private final Product PRODUCT_PREPARED_2 = Product.builder()
            .id(2L)
            .name("Burguer")
            .price(2500.0)
            .description("Double Cheddar")
            .stock(5)
            .image("example.png")
            .category(category)
            .orderDetail(new HashSet<>())
            .build();

    private final ProductEdit PRODUCT_EDIT = new ProductEdit(
            "Pizza",
            2000.0,
            "Pepperoni",
            10,
            "example.jpeg",
            1L
    );

    private final ProductCreate PRODUCT_CREATE = new ProductCreate(
            "Empanada",
            1000.0,
            "Carne salada",
            50,
            "empanada.png",
            1L
    );

    @Test
    public void findAll() {
        Mockito.when(productRepository.findAll()).thenReturn(Arrays.asList(PRODUCT_PREPARED, PRODUCT_PREPARED_2));
        List<ProductDto> productos = productoServiceImpl.findAll();

        assertEquals(2, productos.size());
        Mockito.verify(productRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void findById() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(PRODUCT_PREPARED));
        ProductDto productDto = productoServiceImpl.findById(1L);

        assertEquals(1L, productDto.id());
        Mockito.verify(productRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void update(){
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(PRODUCT_PREPARED));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenAnswer(i -> i.getArgument(0));

        ProductDto productDto = productoServiceImpl.update(PRODUCT_EDIT, 1L);
        assertEquals(1L, productDto.id());
        assertEquals("Pizza", productDto.name());
        assertEquals(2000.0, productDto.price());
        assertEquals("Pepperoni", productDto.description());
        assertEquals(10, productDto.stock());

        Mockito.verify(productRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any(Product.class));
    }

    @Test
    void delete() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(PRODUCT_PREPARED));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenAnswer(i -> i.getArgument(0));

        productoServiceImpl.delete(1L);
        assertTrue(PRODUCT_PREPARED.getDeleted());

        Mockito.verify(productRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any(Product.class));
    }

    @Test
    void save(){
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn((PRODUCT_PREPARED));
        productoServiceImpl.save(PRODUCT_CREATE);

        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any(Product.class));
    }
}
