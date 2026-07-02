package com.example.tp_integrador.controllers;

import com.example.tp_integrador.dtos.order.OrderCreateDto;
import com.example.tp_integrador.dtos.orderdetail.OrderDetailCreate;
import com.example.tp_integrador.dtos.order.OrderDto;
import com.example.tp_integrador.dtos.order.OrderEdit;
import com.example.tp_integrador.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAll(){
        return ResponseEntity.ok(orderService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> update(@PathVariable Long id, @RequestBody OrderEdit orderEdit){
        OrderDto orderDto = orderService.update(id, orderEdit);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<OrderDto> create(
            @PathVariable Long userId,
            @RequestBody OrderCreateDto orderCreateDto
            ){
        OrderDto orderDto = orderService.createOrder(
                userId,
                orderCreateDto.paymentMethod(),
                orderCreateDto.items()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
    }
}


