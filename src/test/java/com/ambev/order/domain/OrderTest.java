package com.ambev.order.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testDefaultConstructor() {
        Order order = new Order();

        assertNull(order.getId(), "O ID inicial deve ser nulo.");
        assertNull(order.getCustomerName(), "O nome do cliente inicial deve ser nulo.");
        assertNull(order.getTotalValue(), "O valor total inicial deve ser nulo.");
        assertNull(order.getProducts(), "A lista de produtos inicial deve ser nula.");
        assertNull(order.getExternalOrderId(), "O ID externo inicial deve ser nulo.");
    }

    @Test
    void testGettersAndSetters() {
        // Criação do objeto
        Order order = new Order();

        // Dados para o teste
        Long id = 1L;
        String customerName = "Cliente Teste";
        Double totalValue = 200.0;
        String externalOrderId = "ORD123456";

        OrderProduct product1 = new OrderProduct();
        product1.setId(1L);
        OrderProduct product2 = new OrderProduct();
        product2.setId(2L);
        List<OrderProduct> products = Arrays.asList(product1, product2);

        // Configuração
        order.setId(id);
        order.setCustomerName(customerName);
        order.setTotalValue(totalValue);
        order.setProducts(products);
        order.setExternalOrderId(externalOrderId);

        // Validações
        assertEquals(id, order.getId(), "O ID deve corresponder ao valor definido.");
        assertEquals(customerName, order.getCustomerName(), "O nome do cliente deve corresponder ao valor definido.");
        assertEquals(totalValue, order.getTotalValue(), "O valor total deve corresponder ao valor definido.");
        assertEquals(products, order.getProducts(), "A lista de produtos deve corresponder ao valor definido.");
        assertEquals(externalOrderId, order.getExternalOrderId(), "O ID externo deve corresponder ao valor definido.");
    }

    @Test
    void testSetProducts() {
        // Criação do objeto
        Order order = new Order();

        // Lista de produtos inicial
        OrderProduct product1 = new OrderProduct();
        product1.setId(1L);
        List<OrderProduct> initialProducts = Arrays.asList(product1);

        // Configuração inicial
        order.setProducts(initialProducts);
        assertNotNull(order.getProducts(), "A lista de produtos não deve ser nula após a configuração.");
        assertEquals(1, order.getProducts().size(), "A lista de produtos deve conter um elemento inicialmente.");

        // Atualização da lista de produtos
        OrderProduct product2 = new OrderProduct();
        product2.setId(2L);
        List<OrderProduct> updatedProducts = Arrays.asList(product1, product2);

        order.setProducts(updatedProducts);
        assertEquals(2, order.getProducts().size(), "A lista de produtos deve conter dois elementos após a atualização.");
    }

    @Test
    void testEqualsAndHashCode() {
        // Dois objetos Order com os mesmos dados
        Order order1 = new Order();
        order1.setId(1L);
        order1.setCustomerName("Cliente Teste");
        order1.setTotalValue(100.0);
        order1.setExternalOrderId("ORD123");

        Order order2 = new Order();
        order2.setId(1L);
        order2.setCustomerName("Cliente Teste");
        order2.setTotalValue(100.0);
        order2.setExternalOrderId("ORD123");

        // Modificando um campo
        order2.setExternalOrderId("ORD124");
        assertNotEquals(order1, order2, "Dois pedidos com dados diferentes não devem ser iguais.");
    }
}
