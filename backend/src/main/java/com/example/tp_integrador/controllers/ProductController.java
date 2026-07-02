package com.example.tp_integrador.controllers;

import com.example.tp_integrador.dtos.product.ProductCreate;
import com.example.tp_integrador.dtos.product.ProductDto;
import com.example.tp_integrador.dtos.product.ProductEdit;
import com.example.tp_integrador.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@Valid @PathVariable Long id, @Valid @RequestBody ProductEdit productEdit) {
        ProductDto productDto = productService.update(productEdit, id);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductCreate productCreate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productCreate));
    }
}
