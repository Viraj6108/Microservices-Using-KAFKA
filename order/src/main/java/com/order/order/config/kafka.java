package com.order.order.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.internals.Topic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class kafka {

    @Bean
    public NewTopic order()
    {
        return TopicBuilder.name("order-event").build();
    }
    @Bean
    public NewTopic orderConfirmed(){
        return TopicBuilder.name("order-confirmed").build();
    }

    @Bean
    public NewTopic cancelOrder()
    {
        return TopicBuilder.name("order-cancel").build();
    }

    @Bean
    public NewTopic delivered(){return TopicBuilder.name("order-delivered").build();}

    @Bean
    public NewTopic paymentDetails(){return TopicBuilder.name("payment-detail").build();}

    @Bean
    public NewTopic shipmentDetails(){return TopicBuilder.name("shipment-detail").build();}
}
