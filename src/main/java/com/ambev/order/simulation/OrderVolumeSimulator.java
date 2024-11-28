package com.ambev.order.simulation;

import com.ambev.order.domain.Order;
import com.ambev.order.domain.OrderProduct;
import com.ambev.order.producer.OrderMessageProducer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderVolumeSimulator implements CommandLineRunner {

    private final OrderMessageProducer producer;

    public OrderVolumeSimulator(OrderMessageProducer producer) {
        this.producer = producer;
    }

    @Override
    public void run(String... args) throws Exception {
        int totalMessages = 200_000; // Número total de mensagens
        int batchSize = 10_000;      // Tamanho do lote de envio

        System.out.println("Iniciando simulação de envio de pedidos...");

        for (int i = 1; i <= totalMessages; i++) {
            Order order = createOrder(i);
            producer.sendOrder(order);

            // Exibe progresso a cada 10.000 mensagens
            if (i % batchSize == 0) {
                System.out.printf("Enviadas %d mensagens até agora.%n", i);
            }
        }

        System.out.println("Simulação de envio de pedidos concluída.");
    }

    private Order createOrder(int id) {
        Order order = new Order();
        order.setCustomerName("Cliente " + id);
        order.setTotalValue(Math.random() * 1000);
        order.setExternalOrderId("EXTERNAL_ID_" + id);
        order.setProducts(createProducts(id));
        return order;
    }

    private List<OrderProduct> createProducts(int orderId) {
        List<OrderProduct> products = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            OrderProduct product = new OrderProduct();
            product.setProductName("Produto_" + orderId + "_" + i);
            product.setPrice(Math.random() * 100); // Preço aleatório para o produto
            product.setQuantity((int) (Math.random() * 10) + 1); // Quantidade aleatória entre 1 e 10
            products.add(product);
        }
        return products;
    }
}

