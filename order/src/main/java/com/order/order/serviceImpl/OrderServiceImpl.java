package com.order.order.serviceImpl;

import com.google.gson.Gson;
import com.order.order.entity.Order;
import com.order.order.entity.PaymentDTO;
import com.order.order.entity.Shipment;
import com.order.order.exception.OrderException;
import com.order.order.repository.OrderRepository;
import com.order.order.service.OrderService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

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
        orderRepository.save(orders);String orderConfirmedJson = gson.toJson(orders);
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


    //Get Order Details

    @Override
    public CombinedOrderDetails getOrderDetails(int orderId) throws OrderException{
        Order order = orderRepository.findById(orderId).orElseThrow(()->
            new OrderException("Order not found"));
         getPaymentDetails(orderId);
         getShipmentDetails(orderId);
         CombinedOrderDetails combinedOrderDetails = new CombinedOrderDetails();
         combinedOrderDetails = listenToKafkaTopic();
         combinedOrderDetails.setOrderDetails(order);
        return combinedOrderDetails;
    }

 public void getPaymentDetails(int orderId)
 {
     String paymentJson = gson.toJson(orderId);
     kafkaTemplate.send("payment-detail",paymentJson);
 }
 public void getShipmentDetails(int orderId)
 {
     String shipmentJson = gson.toJson(orderId);
     kafkaTemplate.send("shipment-detail",shipmentJson);
 }
public CombinedOrderDetails listenToKafkaTopic() {

    Properties properties = new Properties();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "order-group");
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    long runDuration = 10000;
    long startTime = System.currentTimeMillis();

    CombinedOrderDetails combinedOrderDetails = new CombinedOrderDetails();
    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {
        consumer.subscribe(Arrays.asList("shipment-order", "payment-order"));
        System.out.println("Subscribed to topics: shipment-order and payment-order");

        //This while loop will execute only 10 seconds to fetch data from topic
        while (System.currentTimeMillis() - startTime < runDuration) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                // Process the received message
                System.out.printf("Received message: key = %s, value = %s, partition = %s, offset = %s%n",
                        record.key(), record.value(), record.partition(), record.offset());

                if(record.key().equals("payment"))
                {
                    combinedOrderDetails.setPaymentDetails(new Gson().fromJson(record.value(), PaymentDTO.class));
                }else if(record.key().equals("shipment"))
                {
                    combinedOrderDetails.setShipment(new Gson().fromJson(record.value(), Shipment.class));
                }
                System.out.println("Assigned partitions: " + consumer.assignment());
            }

            // Optional: Add a condition to break the loop if needed
            // For example, break after processing a certain number of records
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return combinedOrderDetails; // This will only be reached if the loop is exited
}
}
