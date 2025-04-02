package com.order.order.config;

import org.apache.kafka.clients.admin.NewTopic;
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
}
