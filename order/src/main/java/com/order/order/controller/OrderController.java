package com.order.order.controller;



import com.order.order.entity.Order;
import com.order.order.exception.OrderException;
import com.order.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;



    @PostMapping("/create")
    public Order placeOrder(@RequestBody Order order)throws OrderException
    {
        Order order1 = orderService.placeOrder(order);


        return order1;
    }
}
