package com.ambev.order.consumer;

import com.ambev.order.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DLQMessageConsumer {

    @RabbitListener(queues = RabbitMQConfig.DLQ)
    public void receiveDeadLetterMessage(String message) {
        System.err.println("Mensagem recebida na DLQ: " + message);
    }
}
