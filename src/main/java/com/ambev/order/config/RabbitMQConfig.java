package com.ambev.order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String MAIN_QUEUE = "ordersQueue";
    public static final String DLQ = "ordersDLQ";
    public static final String EXCHANGE = "ordersExchange";
    public static final String MAIN_ROUTING_KEY = "ordersRoutingKey";
    public static final String DLQ_ROUTING_KEY = "ordersDLQRoutingKey";
    public static final String EXCHANGE_NAME = "ordersExchange";
    public static final String ROUTING_KEY = "ordersRoutingKey";

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter); // Define o conversor de mensagens para o listener
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter); // Define o conversor de mensagens
        return rabbitTemplate;
    }

    // Configuração da fila principal com DLQ associada
    @Bean
    public Queue ordersQueue() {
        return QueueBuilder.durable(MAIN_QUEUE)
                .deadLetterExchange(EXCHANGE) // Define o exchange para redirecionar mensagens
                .deadLetterRoutingKey(DLQ_ROUTING_KEY) // Define o routing key para a DLQ
                .build();
    }

    // Configuração da DLQ
    @Bean
    public Queue ordersDLQ() {
        return QueueBuilder.durable(DLQ).build();
    }

    // Configuração do Exchange
    @Bean
    public TopicExchange ordersExchange() {
        return new TopicExchange(EXCHANGE);
    }

    // Binding da fila principal ao Exchange
    @Bean
    public Binding bindingOrdersQueue(Queue ordersQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(ordersQueue).to(ordersExchange).with(MAIN_ROUTING_KEY);
    }

    // Binding da DLQ ao Exchange
    @Bean
    public Binding bindingOrdersDLQ(Queue ordersDLQ, TopicExchange ordersExchange) {
        return BindingBuilder.bind(ordersDLQ).to(ordersExchange).with(DLQ_ROUTING_KEY);
    }
}
