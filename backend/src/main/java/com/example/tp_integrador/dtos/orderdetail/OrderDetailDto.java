package com.example.tp_integrador.dtos.orderdetail;

import com.example.tp_integrador.dtos.product.ProductDto;
import com.example.tp_integrador.entities.OrderDetail;

public record OrderDetailDto(
        Long id,
        Integer quantity,
        Double subtotal,
        ProductDto productDto
) {
    public static OrderDetailDto toDto(OrderDetail detail){
        if (detail == null) return null ;
        return new OrderDetailDto(
                detail.getId(),
                detail.getQuantity(),
                detail.getSubtotal(),
                ProductDto.toDto(detail.getProduct())
        );
    }
}
