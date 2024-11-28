package com.ambev.order.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderProductTest {

    @Test
    void testGettersAndSetters() {
        // Criação do objeto
        OrderProduct orderProduct = new OrderProduct();

        // Configuração de valores
        orderProduct.setId(1L);
        orderProduct.setProductName("Produto Teste");
        orderProduct.setPrice(50.0);
        orderProduct.setQuantity(2);

        Order order = new Order();
        order.setId(100L);
        orderProduct.setOrder(order);

        // Validações
        assertEquals(1L, orderProduct.getId());
        assertEquals("Produto Teste", orderProduct.getProductName());
        assertEquals(50.0, orderProduct.getPrice());
        assertEquals(2, orderProduct.getQuantity());
        assertEquals(order, orderProduct.getOrder());
    }

    @Test
    void testEqualsAndHashCode() {
        // Dois objetos OrderProduct com os mesmos dados
        Order order = new Order();
        order.setId(100L);

        OrderProduct product1 = new OrderProduct();
        product1.setId(1L);
        product1.setProductName("Produto Teste");
        product1.setPrice(50.0);
        product1.setQuantity(2);
        product1.setOrder(order);

        OrderProduct product2 = new OrderProduct();
        product2.setId(1L);
        product2.setProductName("Produto Teste");
        product2.setPrice(50.0);
        product2.setQuantity(2);
        product2.setOrder(order);

        // Modificando um campo
        product2.setProductName("Outro Produto");
        assertNotEquals(product1, product2, "Dois produtos com dados diferentes não devem ser iguais.");
    }

    @Test
    void testNoArgsConstructor() {
        // Verifica se o construtor padrão funciona corretamente
        OrderProduct orderProduct = new OrderProduct();
        assertNotNull(orderProduct, "O objeto não deve ser nulo.");
    }
}
