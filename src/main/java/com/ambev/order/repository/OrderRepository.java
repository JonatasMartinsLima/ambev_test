package com.ambev.order.repository;

import com.ambev.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByExternalOrderId(String externalOrderId);
}
