package com.example.tp_integrador.services;

import com.example.tp_integrador.dtos.orderdetail.OrderDetailCreate;
import com.example.tp_integrador.dtos.order.OrderDto;
import com.example.tp_integrador.dtos.order.OrderEdit;

import java.util.List;

public interface OrderService {

    public OrderDto createOrder(Long userId, String paymentMethod, List<OrderDetailCreate> items);
    public OrderDto update(Long id, OrderEdit orderEdit);
    public OrderDto findById(Long id);
    public List<OrderDto> findAll();
    public void delete(Long id);
}
