package com.order.order.serviceImpl;

import com.google.gson.Gson;
import com.order.order.entity.Order;
import com.order.order.exception.OrderException;
import com.order.order.repository.OrderRepository;
import com.order.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    private final KafkaTemplate kafkaTemplate;

    public OrderServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @Autowired
    private OrderRepository orderRepository;

    Gson gson = new Gson();

    @Override
    public Order placeOrder(Order order) throws OrderException {
        if(order.getItem()==null || order.getQuantity()==null)
        {
            throw new OrderException("Select product and required quantity before placing order");
        }

        order.setStatus(Order.STATUS.PENDING);
        orderRepository.save(order);
        String orderJson = new Gson().toJson(order);
        kafkaTemplate.send("order-event",orderJson);

        return order;
    }

    @Override
    public Order getOrder(int orderId) throws OrderException {
        return null;
    }

    @KafkaListener(topics = "payment-event", groupId="order-group")
    @Override
    public String paymentEvent(String paymentJson) throws OrderException {
        System.out.println("📩 Payment confirmation  Received in Payment Service: " + paymentJson);
        if(paymentJson.equals(null))
        {
            throw new OrderException("Something wrong with payment");
        }
        Order paymentSuccess = gson.fromJson(paymentJson,Order.class);
        System.out.println("📩 Payment confirmation  Received in Payment Service: " + paymentSuccess);
        Order orders = orderRepository.findById(paymentSuccess.getOrderId()).get();
        orders.setStatus(Order.STATUS.CONFIRMED);
        orderRepository.save(orders);
        String orderConfirmedJson = gson.toJson(orders);
        kafkaTemplate.send("order-confirmed",orderConfirmedJson);
        return "Order placed successful";
    }

    @Override
    public Order cancelOrder(int orderId) throws OrderException {
        Order order1 = orderRepository.findById(orderId).get();
        if(order1.getStatus().equals(Order.STATUS.CONFIRMED))
        {
            order1.setStatus(Order.STATUS.CANCELLED);
            orderRepository.save(order1);
            String orderJson = gson.toJson(order1);
            kafkaTemplate.send("order-cancel",orderJson);
            return order1;
        }
        throw new OrderException("Order is CANCELLED");

    }
}
