package com.lowang.ormquerydsl.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowang.ormquerydsl.domain.Order;
import com.lowang.ormquerydsl.repository.OrderRepository;
import com.lowang.ormquerydsl.util.Sequence;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceImpl implements OrderService {
  @Autowired private OrderRepository o;

  @Override
  public Mono<Order> createOrder(Order order) {
    order.setOrderId(Sequence.get().nextId());
    order.setModifiedDate(new Date());
    order.setCreatedDate(new Date());
    return Mono.create(cb -> cb.success(o.save(order)));
  }

  @Override
  public Mono<Order> findByOrderId(Long orderId) {
    return Mono.create(cb -> cb.success(o.findByOrderId(orderId)));
  }

  @Override
  public Flux<Order> findByUserId(Long userId) {
    return Flux.fromIterable(o.findByUserId(userId));
  }
}
