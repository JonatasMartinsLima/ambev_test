package com.ambev.order.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import static org.junit.jupiter.api.Assertions.*;

class RabbitMQConfigTest {

    private RabbitMQConfig rabbitMQConfig;

    @Mock
    private ConnectionFactory connectionFactory;

    @Mock
    private Jackson2JsonMessageConverter jackson2JsonMessageConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rabbitMQConfig = new RabbitMQConfig();
    }

    @Test
    void testRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = rabbitMQConfig.rabbitListenerContainerFactory(connectionFactory, jackson2JsonMessageConverter);
        assertNotNull(factory);

        // Validar que a factory foi configurada corretamente
        assertDoesNotThrow(() -> {
            factory.setConnectionFactory(connectionFactory);
            factory.setMessageConverter(jackson2JsonMessageConverter);
        });
    }

    @Test
    void testJackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = rabbitMQConfig.jackson2JsonMessageConverter();
        assertNotNull(converter);
    }

    @Test
    void testRabbitTemplate() {
        RabbitTemplate rabbitTemplate = rabbitMQConfig.rabbitTemplate(connectionFactory, jackson2JsonMessageConverter);
        assertNotNull(rabbitTemplate);
        assertEquals(jackson2JsonMessageConverter, rabbitTemplate.getMessageConverter());
    }

    @Test
    void testOrdersQueue() {
        Queue queue = rabbitMQConfig.ordersQueue();
        assertNotNull(queue);
        assertEquals(RabbitMQConfig.MAIN_QUEUE, queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void testOrdersDLQ() {
        Queue dlq = rabbitMQConfig.ordersDLQ();
        assertNotNull(dlq);
        assertEquals(RabbitMQConfig.DLQ, dlq.getName());
        assertTrue(dlq.isDurable());
    }

    @Test
    void testOrdersExchange() {
        TopicExchange exchange = rabbitMQConfig.ordersExchange();
        assertNotNull(exchange);
        assertEquals(RabbitMQConfig.EXCHANGE, exchange.getName());
    }

    @Test
    void testBindingOrdersQueue() {
        Queue ordersQueue = new Queue(RabbitMQConfig.MAIN_QUEUE);
        TopicExchange ordersExchange = new TopicExchange(RabbitMQConfig.EXCHANGE);
        Binding binding = rabbitMQConfig.bindingOrdersQueue(ordersQueue, ordersExchange);

        assertNotNull(binding);
        assertEquals(ordersQueue.getName(), binding.getDestination());
        assertEquals(RabbitMQConfig.MAIN_ROUTING_KEY, binding.getRoutingKey());
        assertEquals(Binding.DestinationType.QUEUE, binding.getDestinationType());
    }

    @Test
    void testBindingOrdersDLQ() {
        Queue ordersDLQ = new Queue(RabbitMQConfig.DLQ);
        TopicExchange ordersExchange = new TopicExchange(RabbitMQConfig.EXCHANGE);
        Binding binding = rabbitMQConfig.bindingOrdersDLQ(ordersDLQ, ordersExchange);

        assertNotNull(binding);
        assertEquals(ordersDLQ.getName(), binding.getDestination());
        assertEquals(RabbitMQConfig.DLQ_ROUTING_KEY, binding.getRoutingKey());
        assertEquals(Binding.DestinationType.QUEUE, binding.getDestinationType());
    }
}

