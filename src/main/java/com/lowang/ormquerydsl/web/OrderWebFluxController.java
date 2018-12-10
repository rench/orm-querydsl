package com.lowang.ormquerydsl.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lowang.ormquerydsl.domain.Order;
import com.lowang.ormquerydsl.service.OrderService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
public class OrderWebFluxController {

  @Autowired OrderService s;

  @GetMapping("/{id}")
  public Mono<Order> findByOrderId(@PathVariable Long id) {
    return s.findByOrderId(id);
  }

  @PostMapping
  public Mono<Order> create(Order order) {
    return s.createOrder(order);
  }

  @GetMapping
  public Flux<Order> list(Long userId) {
    return s.findByUserId(userId);
  }
}
