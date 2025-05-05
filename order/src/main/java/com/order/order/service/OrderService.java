package com.order.order.service;

import com.order.order.entity.Order;
import com.order.order.exception.OrderException;
import com.order.order.serviceImpl.CombinedOrderDetails;

import java.util.List;
import java.util.Map;

public interface OrderService {
    public Order placeOrder(Order order) throws OrderException;

    //Get Order Details
    CombinedOrderDetails getOrderDetails(int orderId) throws OrderException;
    public String paymentEvent(String paymentJson)throws OrderException;
    public Order cancelOrder (int order) throws OrderException;

}
