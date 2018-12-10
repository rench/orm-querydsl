package com.lowang.ormquerydsl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lowang.ormquerydsl.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  Order findByOrderId(Long orderId);

  List<Order> findByUserId(Long userId);
}
