package com.ambev.order.producer;

import com.ambev.order.config.RabbitMQConfig;
import com.ambev.order.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderMessageProducerTest {

    private RabbitTemplate rabbitTemplate;
    private OrderMessageProducer producer;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        producer = new OrderMessageProducer(rabbitTemplate);
    }

    @Test
    void testSendOrder() {
        // Mock do pedido
        Order order = new Order();
        order.setId(1L);
        order.setCustomerName("Cliente Teste");
        order.setTotalValue(100.0);

        // Captura os argumentos enviados ao RabbitTemplate
        ArgumentCaptor<MessagePostProcessor> postProcessorCaptor = ArgumentCaptor.forClass(MessagePostProcessor.class);

        // Executa o método
        producer.sendOrder(order);

        // Verifica se o método convertAndSend foi chamado
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE_NAME),
                eq(RabbitMQConfig.ROUTING_KEY),
                eq(order),
                postProcessorCaptor.capture()
        );

        // Valida o MessagePostProcessor
        MessagePostProcessor postProcessor = postProcessorCaptor.getValue();
        assertNotNull(postProcessor, "O MessagePostProcessor não deve ser nulo.");

        // Simula um objeto Message para validar as configurações
        MessageProperties messageProperties = new MessageProperties();
        Message mockMessage = new Message(new byte[0], messageProperties);

        // Aplica o postProcessor no mockMessage
        Message processedMessage = postProcessor.postProcessMessage(mockMessage);

        // Verifica as propriedades configuradas
        assertEquals("application/json", processedMessage.getMessageProperties().getContentType(),
                "O contentType deve ser 'application/json'.");
    }
}
