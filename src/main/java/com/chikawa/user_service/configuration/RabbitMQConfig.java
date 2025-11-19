package com.chikawa.user_service.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE = "user_exchange";
    public static final String PROMOTION_QUEUE = "user_promotion_queue";
    public static final String CART_QUEUE = "product_cart_queue";

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE);
    }

    @Bean
    public Queue searchQueue() {
        return new Queue(PROMOTION_QUEUE, true);
    }

    @Bean
    public Queue cartQueue() {
        return new Queue(CART_QUEUE, true);
    }

    @Bean
    public Binding bindSearchQueue(Queue searchQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(searchQueue).to(fanoutExchange);
    }

    @Bean
    public Binding bindCartQueue(Queue cartQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(cartQueue).to(fanoutExchange);
    }
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

}



