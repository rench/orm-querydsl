package com.lowang.ormquerydsl.service;

import com.lowang.ormquerydsl.domain.Order;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {

  Mono<Order> createOrder(Order order);

  Mono<Order> findByOrderId(Long orderId);

  Flux<Order> findByUserId(Long userId);
}
