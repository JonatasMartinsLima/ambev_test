package com.ambev.order.controller;

import com.ambev.order.domain.Order;
import com.ambev.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Test
    void testGetAllOrders() {
        // Mock do serviço
        OrderService service = mock(OrderService.class);

        // Mock dos pedidos
        Order order1 = new Order();
        order1.setId(1L);
        order1.setTotalValue(100.0);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setTotalValue(200.0);

        List<Order> orders = Arrays.asList(order1, order2);

        // Configura o mock para retornar os pedidos
        when(service.findAll()).thenReturn(orders);

        // Instância do controlador
        OrderController controller = new OrderController(service);

        // Executa o método
        List<Order> result = controller.getAllOrders();

        // Validações
        assertNotNull(result, "A lista de pedidos não deve ser nula.");
        assertEquals(2, result.size(), "A lista de pedidos deve conter 2 elementos.");
        assertEquals(order1, result.get(0), "O primeiro pedido deve ser igual ao esperado.");
        assertEquals(order2, result.get(1), "O segundo pedido deve ser igual ao esperado.");
        verify(service, times(1)).findAll(); // Garante que o método do serviço foi chamado
    }

    @Test
    void testCreateOrder() {
        // Mock do serviço
        OrderService service = mock(OrderService.class);

        // Mock do pedido
        Order inputOrder = new Order();
        inputOrder.setId(1L);
        inputOrder.setTotalValue(100.0);

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setTotalValue(100.0);

        // Configura o mock para salvar o pedido
        when(service.save(inputOrder)).thenReturn(savedOrder);

        // Instância do controlador
        OrderController controller = new OrderController(service);

        // Executa o método
        Order result = controller.createOrder(inputOrder);

        // Validações
        assertNotNull(result, "O pedido salvo não deve ser nulo.");
        assertEquals(savedOrder, result, "O pedido retornado deve ser igual ao esperado.");
        verify(service, times(1)).save(inputOrder); // Garante que o método do serviço foi chamado
    }
}
