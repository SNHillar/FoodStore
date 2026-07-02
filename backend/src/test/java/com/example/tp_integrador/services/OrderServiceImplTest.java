package com.example.tp_integrador.services;

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
import com.example.tp_integrador.services.impl.OrderServiceImpl;
import com.example.tp_integrador.services.impl.ProductServiceImpl;
import com.example.tp_integrador.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    OrderServiceImpl pedidoServiceImpl;

    @InjectMocks
    ProductServiceImpl productoServiceImpl;

    @InjectMocks
    UserServiceImpl usuarioServiceImpl;


    User USER_TEST = User.builder().id(1L).build();

    private final Order ORDER_PREPARED = Order.builder()
            .id(1L)
            .status(Status.PENDING)
            .payment(Payment.CARD)
            .details(Set.of())
            .user(USER_TEST)
            .build();

    private final OrderEdit ORDER_EDIT = new OrderEdit(
            "cancelado",
            "tarjeta"
    );

    @Test
    void findById() {
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(ORDER_PREPARED));
        OrderDto orderDto = pedidoServiceImpl.findById(1L);

        assertEquals(1L, orderDto.id());
        Mockito.verify(orderRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void findByIdNotFound() {
        Mockito.when(orderRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> pedidoServiceImpl.findById(99L));
    }

    @Test
    void findAll() {
        Mockito.when(orderRepository.findAll()).thenReturn(Arrays.asList(ORDER_PREPARED));
        List<OrderDto> orderDto = pedidoServiceImpl.findAll();

        assertEquals(1, orderDto.size());
        Mockito.verify(orderRepository, Mockito.times(1)).findAll();
    }

    @Test
    void create(){
        Long userId = 1L;
        Long productId = 2L;

        OrderDetailCreate orderDetailCreate = new OrderDetailCreate(productId, 1L, 2);
        List<OrderDetailCreate> orderDetailCreateList = Arrays.asList(orderDetailCreate);

        User user = User.builder()
                .id(userId)
                .firstName("user")
                .build();

        Product product = Product.builder()
                .id(productId)
                .name("product")
                .price(1400.0)
                .stock(10)
                .build();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenAnswer(i -> i.getArgument(0));

        OrderDto orderDto = pedidoServiceImpl.createOrder(userId, "CARD", orderDetailCreateList);

        assertNotNull(orderDto);
        assertEquals(userId, orderDto.userId());
        assertEquals(8, product.getStock());

        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verify(productRepository, Mockito.times(1)).findById(productId);
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any(Product.class));
        Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any(Order.class));
    }

    @Test
    void update() {
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(ORDER_PREPARED));
        OrderDto orderDto = pedidoServiceImpl.update(1L, ORDER_EDIT);

        assertEquals(1L, orderDto.id());
        assertEquals("CANCELED", orderDto.status());

        Mockito.verify(orderRepository, Mockito.times(1)).findById(1L);
    }

}
