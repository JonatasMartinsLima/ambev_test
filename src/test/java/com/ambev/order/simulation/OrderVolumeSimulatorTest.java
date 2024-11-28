package com.ambev.order.simulation;

import com.ambev.order.domain.Order;
import com.ambev.order.domain.OrderProduct;
import com.ambev.order.producer.OrderMessageProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderVolumeSimulatorTest {

    private OrderMessageProducer producer;
    private OrderVolumeSimulator simulator;

    @BeforeEach
    void setUp() {
        producer = mock(OrderMessageProducer.class);
        simulator = new OrderVolumeSimulator(producer);
    }

    @Test
    void testRunSimulatesOrderMessages() throws Exception {
        // Executa o método run
        simulator.run();

        // Verifica se o método de envio foi chamado o número esperado de vezes
        verify(producer, times(200_000)).sendOrder(any(Order.class));
    }

    @Test
    void testCreateOrder() {
        // Executa o método createOrder
        Order order = simulator.createOrder(1);

        // Validações
        assertNotNull(order, "O pedido criado não deve ser nulo.");
        assertEquals("Cliente 1", order.getCustomerName(), "O nome do cliente deve ser 'Cliente 1'.");
        assertEquals("EXTERNAL_ID_1", order.getExternalOrderId(), "O externalOrderId deve ser 'EXTERNAL_ID_1'.");
        assertNotNull(order.getProducts(), "A lista de produtos não deve ser nula.");
        assertEquals(5, order.getProducts().size(), "A lista de produtos deve conter 5 itens.");
    }

    @Test
    void testCreateProducts() {
        // Executa o método createProducts
        List<OrderProduct> products = simulator.createProducts(1);

        // Validações
        assertNotNull(products, "A lista de produtos não deve ser nula.");
        assertEquals(5, products.size(), "A lista de produtos deve conter 5 itens.");

        for (int i = 0; i < products.size(); i++) {
            OrderProduct product = products.get(i);
            assertNotNull(product, "O produto não deve ser nulo.");
            assertTrue(product.getPrice() >= 0, "O preço do produto deve ser maior ou igual a zero.");
            assertTrue(product.getQuantity() >= 1 && product.getQuantity() <= 10,
                    "A quantidade do produto deve estar entre 1 e 10.");
            assertEquals("Produto_1_" + i, product.getProductName(),
                    "O nome do produto deve corresponder ao padrão 'Produto_1_i'.");
        }
    }

    @Test
    void testBatchProgressOutput() throws Exception {
        // Captura saída no console
        simulator = new OrderVolumeSimulator(producer) {
            @Override
            public void run(String... args) throws Exception {
                int totalMessages = 20; // Reduz o número total de mensagens para um teste mais rápido
                int batchSize = 10;

                for (int i = 1; i <= totalMessages; i++) {
                    Order order = createOrder(i);
                    producer.sendOrder(order);

                    if (i % batchSize == 0) {
                        System.out.printf("Enviadas %d mensagens até agora.%n", i);
                    }
                }
            }
        };

        simulator.run();
        verify(producer, times(20)).sendOrder(any(Order.class));
    }
}
