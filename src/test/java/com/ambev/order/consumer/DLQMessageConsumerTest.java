package com.ambev.order.consumer;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DLQMessageConsumerTest {

    @Test
    void testReceiveDeadLetterMessage() {
        // Captura o System.err para verificar a saída
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        System.setErr(Mockito.mock(java.io.PrintStream.class));

        // Instância do consumidor
        DLQMessageConsumer consumer = new DLQMessageConsumer();

        // Mensagem de teste
        String testMessage = "Teste de DLQ";

        // Executa o método
        consumer.receiveDeadLetterMessage(testMessage);

        // Verifica se a mensagem foi impressa no console de erro
        verify(System.err).println(captor.capture());
        assertTrue(captor.getValue().contains("Mensagem recebida na DLQ: Teste de DLQ"));
    }
}
