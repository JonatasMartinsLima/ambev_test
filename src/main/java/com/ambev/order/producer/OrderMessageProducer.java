package com.ambev.order.producer;

import com.ambev.order.domain.Order;
import com.ambev.order.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public OrderMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrder(Order order) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                order,
                message -> {
                    message.getMessageProperties().setContentType("application/json");
                    return message;
                }
        );
        System.out.println("Pedido enviado para a fila: " + order);
    }
}

