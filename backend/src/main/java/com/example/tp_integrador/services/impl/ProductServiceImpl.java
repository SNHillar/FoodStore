package com.example.tp_integrador.services.impl;

import com.example.tp_integrador.dtos.product.ProductCreate;
import com.example.tp_integrador.dtos.product.ProductDto;
import com.example.tp_integrador.dtos.product.ProductEdit;
import com.example.tp_integrador.entities.Product;
import com.example.tp_integrador.repositories.ProductRepository;
import com.example.tp_integrador.services.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto save(ProductCreate productCreate) {
        Product product = productCreate.toEntity();
        product = productRepository.save(product);
        return ProductDto.toDto(product);
    }

    @Override
    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow( () -> new NullPointerException("Product not found with id:" + id));
        return ProductDto.toDto(product);
    }

    @Override
    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductDto::toDto).toList();
    }

    @Override
    public ProductDto update(ProductEdit productEdit, Long id) {
        Product product = productRepository.findById(id).orElseThrow( () -> new NullPointerException("Product not found with id: " + id));
        productEdit.applyTo(product, product.getCategory());
        productRepository.save(product);
        return ProductDto.toDto(product);
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NullPointerException("Product not found with id: " + id));
        product.setDeleted(true);
        productRepository.save(product);
    }
}
