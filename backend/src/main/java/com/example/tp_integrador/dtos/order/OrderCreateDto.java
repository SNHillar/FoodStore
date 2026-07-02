package com.example.tp_integrador.dtos.order;

import com.example.tp_integrador.dtos.orderdetail.OrderDetailCreate;

import java.util.List;

public record OrderCreateDto(
        String paymentMethod,
        List<OrderDetailCreate> items
) {

}
