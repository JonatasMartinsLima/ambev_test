package com.ambev.order.consumer;

import com.ambev.order.domain.Order;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderMessageConsumerTest {

    @Test
    void testReceiveOrderWithValidOrder() {
        // Captura o System.out
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        System.setOut(Mockito.mock(java.io.PrintStream.class));

        // Instância do consumidor
        OrderMessageConsumer consumer = new OrderMessageConsumer();

        // Cria um pedido válido
        Order validOrder = new Order();
        validOrder.setId(1L);
        validOrder.setTotalValue(100.0);

        // Executa o método
        consumer.receiveOrder(validOrder);

        // Verifica a mensagem no console
        verify(System.out).println(captor.capture());
        assertTrue(captor.getValue().contains("Pedido processado: " + validOrder));
    }

    @Test
    void testReceiveOrderWithInvalidOrder() {
        // Captura o System.err
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        System.setErr(Mockito.mock(java.io.PrintStream.class));

        // Instância do consumidor
        OrderMessageConsumer consumer = new OrderMessageConsumer();

        // Cria um pedido inválido
        Order invalidOrder = new Order();
        invalidOrder.setId(2L);
        invalidOrder.setTotalValue(-50.0);

        // Executa o método e espera uma exceção
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            consumer.receiveOrder(invalidOrder);
        });

        // Verifica a mensagem de erro no console
        verify(System.err).println(captor.capture());
        assertTrue(captor.getValue().contains("Erro ao processar o pedido: " + exception.getMessage()));
        assertEquals("Valor total do pedido inválido!", exception.getMessage());
    }
}
