package com.ambev.order.controller;

import com.ambev.order.domain.Order;
import com.ambev.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Gestão de pedidos")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos os pedidos", description = "Retorna a lista de todos os pedidos.")
    public List<Order> getAllOrders() {
        return service.findAll();
    }

    @PostMapping
    @Operation(summary = "Criar um novo pedido", description = "Cria um novo pedido com os dados fornecidos.")
    public Order createOrder(@RequestBody Order order) {
        return service.save(order);
    }
}