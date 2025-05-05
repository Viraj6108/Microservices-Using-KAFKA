package com.order.order.serviceImpl;

import com.google.gson.Gson;
import com.order.order.entity.Order;
import com.order.order.entity.Shipment;
import com.order.order.exception.OrderException;
import com.order.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private OrderRepository orderRepository;
    private Gson gson=new Gson();

    private KafkaTemplate kafkaTemplate;
    public NotificationService(KafkaTemplate<String,String> kafkaTemplate)
    {
        this.kafkaTemplate = kafkaTemplate;
    }
    @KafkaListener(topics = "shipment-delivered", groupId = "order-group")
    public void shipmentEmail(String shipmentJson) throws OrderException
    {
       Shipment shipment = gson.fromJson(shipmentJson, Shipment.class);
        Order order = orderRepository.findById(shipment.getOrderId()).get();
        if(order.getStatus().equals("CANCELLED"))
        {
            throw new OrderException("Order is cancelled please Place order again ");
        }
        String orderJson = gson.toJson(order);
        kafkaTemplate.send("order-delivered",orderJson);
    }
}
