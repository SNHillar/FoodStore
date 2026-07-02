package com.example.tp_integrador.dtos.orderdetail;

import com.example.tp_integrador.entities.OrderDetail;
import com.example.tp_integrador.entities.Order;
import com.example.tp_integrador.entities.Product;

public record
OrderDetailCreate(
        Long productId,
        Long orderId,
        Integer quantity
){
    public OrderDetail toEntity(){
        return OrderDetail.builder()
                .product(Product.builder().id(productId).build())
                .order(Order.builder().id(orderId).build())
                .quantity(quantity)
                .build();
    }
}
