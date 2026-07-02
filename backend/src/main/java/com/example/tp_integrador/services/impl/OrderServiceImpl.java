package com.example.tp_integrador.services.impl;

import com.example.tp_integrador.dtos.orderdetail.OrderDetailCreate;
import com.example.tp_integrador.dtos.order.OrderDto;
import com.example.tp_integrador.dtos.order.OrderEdit;
import com.example.tp_integrador.entities.Order;
import com.example.tp_integrador.entities.Product;
import com.example.tp_integrador.entities.User;
import com.example.tp_integrador.enums.Status;
import com.example.tp_integrador.enums.Payment;
import com.example.tp_integrador.repositories.OrderRepository;
import com.example.tp_integrador.repositories.ProductRepository;
import com.example.tp_integrador.repositories.UserRepository;
import com.example.tp_integrador.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public OrderDto createOrder(Long userId, String paymentMethod, List<OrderDetailCreate> items) {
        // Create a new empty order and initialize it with pending status, date and the payment method chosen by the client
        Order order = new Order();
        order.setDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);
        order.setPayment(Payment.valueOf(paymentMethod.trim().toUpperCase()));

        // Find and set the user who created the order
        User user = userRepository.findById(userId).orElseThrow(()-> new NullPointerException("User not found with id: " + userId));
        order.setUser(user);
        order.setDetails(new HashSet<>());
        // Iterate over the list of requested details and validate/subtract stock
        for (OrderDetailCreate item : items){
            Product product = productRepository.findById(item.productId()).orElseThrow(() -> new NullPointerException("Product not found with id: " + item.productId()));
            if (product.getStock() < item.quantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }
            // If there is stock, subtract it from the product and save the new quantity to the repository
            product.setStock(product.getStock() - item.quantity());
            productRepository.save(product);
            // Add the requested details to the order
            order.addDetail(item.quantity(), product);
        }
        // Calculate the total with the entity method and save the order in the repository
        order.calcularTotal();
        Order savedOrder = orderRepository.save(order);
        return OrderDto.toDto(savedOrder);
    }

    @Override
    public OrderDto update(Long id, OrderEdit orderEdit) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NullPointerException("Order not found with id: " + id));
        orderEdit.applyTo(order);
        orderRepository.save(order);
        return OrderDto.toDto(order);
    }

    @Override
    public OrderDto findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
        return OrderDto.toDto(order);
    }

    @Override
    public List<OrderDto> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(OrderDto::toDto).toList();
    }

    @Override
    public void delete(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NullPointerException("Order not found with id: " + id));
        order.setDeleted(true);
        orderRepository.save(order);
    }
}
