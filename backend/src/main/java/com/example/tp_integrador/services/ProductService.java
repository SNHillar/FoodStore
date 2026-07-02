package com.example.tp_integrador.services;

import com.example.tp_integrador.dtos.product.ProductCreate;
import com.example.tp_integrador.dtos.product.ProductDto;
import com.example.tp_integrador.dtos.product.ProductEdit;

import java.util.List;


public interface ProductService {
    public ProductDto save(ProductCreate productCreate);
    public ProductDto findById(Long id);
    public List<ProductDto> findAll();
    public ProductDto update(ProductEdit productEdit, Long id);
    public void delete(Long id);
}
