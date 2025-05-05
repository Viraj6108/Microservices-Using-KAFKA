package com.order.order.controller;



import com.order.order.entity.Order;
import com.order.order.exception.OrderException;
import com.order.order.service.OrderService;
import com.order.order.serviceImpl.CombinedOrderDetails;
import com.order.order.serviceImpl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderServiceImpl orderServiceImpl;


    @PostMapping("/create")
    public ResponseEntity<Object> placeOrder(@RequestBody Order order)throws OrderException
    {
        Order order1 = orderService.placeOrder(order);
        return ResponseEntity.status(HttpStatus.OK).body(order1);
    }

    //Cancellation of order
    @PostMapping("/cancel")
    public ResponseEntity<Order> cancelOrder(@RequestParam ("orderId") int orderId)throws OrderException
    {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.cancelOrder(orderId));
    }

    @GetMapping(value = "/orderDetails",produces = MediaType.APPLICATION_JSON_VALUE)
    public CombinedOrderDetails getOrderDetails(@RequestParam("orderId") int orderId)throws OrderException
    {
        CombinedOrderDetails Combined = orderService.getOrderDetails(orderId);
        return Combined;
    }

    @GetMapping("/getShipmentDetail")
    public void getShipmentDetails(@RequestParam("orderId")int orderId)
    {
        orderServiceImpl.getShipmentDetails(orderId);
    }
}
