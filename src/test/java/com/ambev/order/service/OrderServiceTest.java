package com.ambev.order.service;

import com.ambev.order.domain.Order;
import com.ambev.order.domain.OrderProduct;
import com.ambev.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository repository;
    private RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;
    private OrderService service;

    @BeforeEach
    void setUp() {
        repository = mock(OrderRepository.class);
        redisTemplate = mock(RedisTemplate.class);
        valueOperations = mock(ValueOperations.class);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        service = new OrderService(repository);
        service.redisTemplate = redisTemplate; // Injeta o mock manualmente
    }

    @Test
    void testFindAll() {
        // Mock da lista de pedidos
        Order order = new Order();
        when(repository.findAll()).thenReturn(List.of(order));

        // Executa o método
        List<Order> orders = service.findAll();

        // Validações
        assertNotNull(orders, "A lista de pedidos não deve ser nula.");
        assertEquals(1, orders.size(), "A lista de pedidos deve conter 1 elemento.");
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindByIdWhenExists() {
        // Mock do pedido encontrado
        Order order = new Order();
        order.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(order));

        // Executa o método
        Order foundOrder = service.findById(1L);

        // Validações
        assertNotNull(foundOrder, "O pedido encontrado não deve ser nulo.");
        assertEquals(1L, foundOrder.getId(), "O ID do pedido encontrado deve ser 1.");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdWhenNotExists() {
        // Mock do pedido não encontrado
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Executa o método
        Order foundOrder = service.findById(1L);

        // Validações
        assertNull(foundOrder, "O pedido encontrado deve ser nulo.");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testSaveWithUniqueExternalOrderId() {
        // Mock do pedido
        Order order = new Order();
        order.setExternalOrderId("ORD123");
        OrderProduct product = new OrderProduct();
        order.setProducts(Arrays.asList(product));

        when(repository.existsByExternalOrderId("ORD123")).thenReturn(false);
        when(redisTemplate.hasKey("externalOrderId::ORD123")).thenReturn(false);
        when(repository.save(order)).thenReturn(order);

        // Executa o método
        Order savedOrder = service.save(order);

        // Validações
        assertNotNull(savedOrder, "O pedido salvo não deve ser nulo.");
        assertEquals(order, savedOrder, "O pedido salvo deve ser o mesmo que o mock.");

        verify(repository, times(1)).existsByExternalOrderId("ORD123");
        verify(redisTemplate, times(1)).hasKey("externalOrderId::ORD123");
        verify(repository, times(1)).save(order);
        verify(redisTemplate.opsForValue(), times(1)).set("externalOrderId::ORD123", order);
        assertEquals(order, product.getOrder(), "O produto deve estar associado ao pedido.");
    }

    @Test
    void testSaveThrowsExceptionWhenOrderIdExistsInRepository() {
        // Mock do pedido
        Order order = new Order();
        order.setExternalOrderId("ORD123");

        when(repository.existsByExternalOrderId("ORD123")).thenReturn(true);

        // Executa o método e valida a exceção
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.save(order));
        assertEquals("Pedido com externalOrderId já existe: ORD123", exception.getMessage());
        verify(repository, times(1)).existsByExternalOrderId("ORD123");
    }

    @Test
    void testSaveThrowsExceptionWhenOrderIdExistsInCache() {
        // Mock do pedido
        Order order = new Order();
        order.setExternalOrderId("ORD123");

        when(repository.existsByExternalOrderId("ORD123")).thenReturn(false);
        when(redisTemplate.hasKey("externalOrderId::ORD123")).thenReturn(true);

        // Executa o método e valida a exceção
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.save(order));
        assertEquals("Pedido com externalOrderId já existe no cache: ORD123", exception.getMessage());
        verify(repository, times(1)).existsByExternalOrderId("ORD123");
        verify(redisTemplate, times(1)).hasKey("externalOrderId::ORD123");
    }

    @Test
    void testSaveAssociatesProductsToOrder() {
        // Mock do pedido e produtos
        Order order = new Order();
        order.setExternalOrderId("ORD123");
        OrderProduct product1 = new OrderProduct();
        OrderProduct product2 = new OrderProduct();
        order.setProducts(Arrays.asList(product1, product2));

        when(repository.existsByExternalOrderId("ORD123")).thenReturn(false);
        when(redisTemplate.hasKey("externalOrderId::ORD123")).thenReturn(false);
        when(repository.save(order)).thenReturn(order);

        // Executa o método
        Order savedOrder = service.save(order);

        // Valida que os produtos estão associados corretamente ao pedido
        assertEquals(order, product1.getOrder());
        assertEquals(order, product2.getOrder());
    }
}
