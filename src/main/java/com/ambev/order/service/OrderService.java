package com.ambev.order.service;

import com.ambev.order.domain.Order;
import com.ambev.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "orders")
    public List<Order> findAll() {
        return repository.findAll();
    }

    public Order findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Order save(Order order) {

        if (repository.existsByExternalOrderId(order.getExternalOrderId())) {
            throw new IllegalArgumentException("Pedido com externalOrderId já existe: " + order.getExternalOrderId());
        }

        if (redisTemplate.hasKey("externalOrderId::" + order.getExternalOrderId())) {
            throw new IllegalArgumentException("Pedido com externalOrderId já existe no cache: " + order.getExternalOrderId());
        }

        order.getProducts().forEach(product -> product.setOrder(order));
        Order savedOrder = repository.save(order);

        redisTemplate.opsForValue().set("externalOrderId::" + order.getExternalOrderId(), savedOrder);

        return savedOrder;
    }

}
