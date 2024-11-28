# Sistema de Gestão de Pedidos com Alta Volumetria

## Visão Geral

Este projeto é um sistema para gerenciar e processar pedidos recebidos de uma fonte externa, calcular o valor total dos
produtos e disponibilizar os resultados para outro sistema. Foi projetado para lidar com alta volumetria de dados, com
suporte a Dead Letter Queue (DLQ) e reprocessamento de mensagens.

---

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **PostgreSQL**: Banco de dados relacional.
- **Redis**: Cache para reduzir a carga no banco.
- **RabbitMQ**: Broker de mensagens para comunicação assíncrona.
- **Docker**: Para gerenciar os serviços auxiliares.
- **Testcontainers**: Testes de integração isolados.
- **Swagger**: Documentação da API.
- **Actuator**: Monitoramento do sistema.

---

## Funcionalidades

1. **Gestão de Pedidos**
    - Receber pedidos de um sistema externo.
    - Calcular o valor total dos produtos.
    - Salvar pedidos e produtos no banco de dados.

2. **Alta Disponibilidade**
    - Cache com Redis para otimizar consultas frequentes.
    - RabbitMQ para processamento assíncrono.

3. **Dead Letter Queue (DLQ)**
    - Armazenar mensagens que falharam no processamento.
    - Reprocessar mensagens da DLQ manualmente ou automaticamente.

4. **Monitoramento e Escalabilidade**
    - Actuator para métricas.
    - Configurações de paralelismo em RabbitMQ.

---

## Requisitos

- **Docker** (para Redis, PostgreSQL e RabbitMQ)
- **Java 17**
- **Maven**

---

## Como Executar

### 1. Iniciar os Serviços com Docker

Execute os seguintes comandos para iniciar os serviços auxiliares:

#### RabbitMQ

```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

#### Redis
```bash
docker run -d --name redis -p 6379:6379 redis

#### PostgreSQL
```bash
docker run -d --name postgres -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres postgres

### 2. Configurar o projeto
Atualize o arquivo application.yml com as configurações dos serviços Docker.

### 3. Iniciar a aplicação
```bash
mvn spring-boot:run

### 4. Endpoints da API
- **GET** /orders: Listar todos os pedidos.
- **POST** /orders: Criar um novo pedido.

### 5. Tstes Unitários
```bash
mvn test

### 6. Testes de Integração
```bash
mvn test

### 7. Monitoramento
Acesse as métricas do sistema:
Actuator Metrics: http://localhost:8080/actuator/metrics
RabbitMQ Management: http://localhost:15672

### 8. Documentação da API
Acesse a documentação da API:
Swagger UI: http://localhost:8080/swagger-ui.html



