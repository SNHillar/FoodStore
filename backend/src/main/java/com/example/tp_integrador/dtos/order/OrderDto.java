package com.example.tp_integrador.dtos.order;

import com.example.tp_integrador.dtos.orderdetail.OrderDetailDto;
import com.example.tp_integrador.entities.Order;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record OrderDto(
        Long id,
        LocalDateTime date,
        String status,
        String paymentMethod,
        Long userId,
        Set<OrderDetailDto> orderDetails
) {
    public static OrderDto toDto (Order order){
        return new OrderDto(
                order.getId(),
                order.getDate(),
                order.getStatus().toString(),
                order.getPayment().toString(),
                order.getUser().getId(),
                order.getDetails().stream()
                        .map(OrderDetailDto::toDto)
                        .collect(Collectors.toSet())
        );
    }
}
