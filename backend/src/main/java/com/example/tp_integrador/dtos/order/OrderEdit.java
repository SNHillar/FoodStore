package com.example.tp_integrador.dtos.order;

import com.example.tp_integrador.entities.Order;
import com.example.tp_integrador.enums.Status;
import com.example.tp_integrador.enums.Payment;

public record OrderEdit(
        String status,
        String paymentMethod
) {
    public void applyTo (Order order){
        if (status != null) {
            order.setStatus(Status.valueOf(this.status.trim().toUpperCase()));
        }
        if (paymentMethod != null) {
            order.setPayment(Payment.valueOf(this.paymentMethod.trim().toUpperCase()));
        }
    }
}
