package com.example.tp_integrador.services;

import com.example.tp_integrador.dtos.category.CategoryCreate;
import com.example.tp_integrador.dtos.category.CategoryDto;
import com.example.tp_integrador.dtos.category.CategoryEdit;
import com.example.tp_integrador.entities.Category;
import com.example.tp_integrador.entities.Product;
import com.example.tp_integrador.repositories.CategoryRepository;
import com.example.tp_integrador.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    private Product PRODUCT_TEST = Product.builder()
            .id(1L)
            .name("Napolitana")
            .description("con tomates")
            .build();

    private Product PRODUCT_TEST_2 = Product.builder()
            .id(2L)
            .name("Calabresa")
            .description("con tomates")
            .build();

    private final Category CATEGORY_PREPARED = Category.builder()
            .id(1L)
            .name("Pizzas")
            .description("Masa Madre")
            .products(Set.of(PRODUCT_TEST, PRODUCT_TEST_2))
            .build();

    private final Category CATEGORY_PREPARED_2 = Category.builder()
            .id(2L)
            .name("Hamburguesas")
            .description("Pan de papa")
            .products(Set.of(PRODUCT_TEST))
            .build();

    private final CategoryEdit CATEGORY_EDIT = new CategoryEdit(
            "Empanadas",
            "Criollas"
    );

    private final CategoryCreate CATEGORY_CREATE = new CategoryCreate(
            "Sandwiches",
            "Panes Artesanales"
    );

    @Test
    public void findAll() {
        Mockito.when(categoryRepository.findAll()).thenReturn(Arrays.asList(CATEGORY_PREPARED, CATEGORY_PREPARED_2));
        List<CategoryDto> categories = categoryServiceImpl.findAll();

        assertEquals(2, categories.size());
        Mockito.verify(categoryRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void findById() {
        Mockito.when(categoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(CATEGORY_PREPARED));
        CategoryDto categoria = categoryServiceImpl.findById(1L);

        assertEquals(1L, categoria.id());
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(1L);

    }

    @Test
    public void save() {
        Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(CATEGORY_PREPARED);
        categoryServiceImpl.save(CATEGORY_CREATE);


        Mockito.verify(categoryRepository, Mockito.times(1)).save(Mockito.any(Category.class));
    }

    @Test
    public void update() {
        Mockito.when(categoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(CATEGORY_PREPARED));
        Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(CATEGORY_PREPARED);

        CategoryDto categoryDto = categoryServiceImpl.update(CATEGORY_EDIT, 1L);

        assertEquals(1L, categoryDto.id());
        assertEquals("Empanadas", categoryDto.name());
        assertEquals("Criollas", categoryDto.description());

        Mockito.verify(categoryRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(categoryRepository, Mockito.times(1)).save(Mockito.any(Category.class));
    }
}
