package com.example.tp_integrador.entities;

import com.example.tp_integrador.enums.Status;
import com.example.tp_integrador.enums.Payment;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString
@Table(name = "orders")
public class Order extends Base implements Calculable{

    @DateTimeFormat
    private LocalDateTime date;

    @EnumeratedValue
    private Status status;

    @EnumeratedValue
    private Payment payment;

    @EqualsAndHashCode.Include
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) // if an order is deleted, its details are deleted. If a detail is deleted, the order is not deleted.
    private Set<OrderDetail> details;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public void calcularTotal() {
        details.stream()
                .mapToDouble(OrderDetail::getSubtotal)
                .sum();
    }

    public OrderDetail findDetailByProduct(Product product){
        return this.details.stream()
                .filter(detail -> detail.getProduct().equals(product))
                .findFirst()
                .orElse(null);
    }

    public void addDetail(Integer quantity, Product product){
        OrderDetail orderDetail = findDetailByProduct(product);
        if(orderDetail != null){
            int newQuantity = orderDetail.getQuantity() + quantity;
            orderDetail.setQuantity(newQuantity);
            orderDetail.setSubtotal(newQuantity * product.getPrice());
        } else {
            OrderDetail newDetail = OrderDetail.builder()
                    .quantity(quantity)
                    .subtotal(quantity * product.getPrice())
                    .order(this)
                    .product(product)
                    .build();
            this.details.add(newDetail);
        }
    }

    public void deleteDetailByProduct(Product product){
        OrderDetail orderDetail = findDetailByProduct(product);
        if(orderDetail != null){
            this.details.remove(orderDetail);
        } else {
            throw new NullPointerException("Order detail not found for product: " + product.getName());
        }
    }
}
