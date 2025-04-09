package com.order.order.service;

import com.order.order.entity.Order;
import com.order.order.exception.OrderException;

public interface OrderService {
    public Order placeOrder(Order order) throws OrderException;
    public Order getOrder(int orderId) throws OrderException;
    public String paymentEvent(String paymentJson)throws OrderException;
    public Order cancelOrder (int order) throws OrderException;

}
