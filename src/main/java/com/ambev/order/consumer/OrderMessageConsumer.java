package com.ambev.order.consumer;

import com.ambev.order.config.RabbitMQConfig;
import com.ambev.order.domain.Order;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageConsumer {

    @RabbitListener(queues = RabbitMQConfig.MAIN_QUEUE)
    public void receiveOrder(Order order) {
        try {
            // Simula um erro no processamento
            if (order.getTotalValue() < 0) {
                throw new IllegalArgumentException("Valor total do pedido inválido!");
            }
            System.out.println("Pedido processado: " + order);
        } catch (Exception e) {
            System.err.println("Erro ao processar o pedido: " + e.getMessage());
            throw e; // Rejeita a mensagem e a envia para a DLQ
        }
    }
}


