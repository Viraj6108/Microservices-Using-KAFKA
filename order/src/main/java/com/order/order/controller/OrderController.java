package com.order.order.controller;



import com.order.order.entity.Order;
import com.order.order.exception.OrderException;
import com.order.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    //Cancellation of order
    @PostMapping("/cancel")
    public Order cancelOrder(@RequestParam ("orderId") int orderId)throws OrderException
    {
        return orderService.cancelOrder(orderId);
    }
}
