package com.lowang.ormquerydsl.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lowang.ormquerydsl.domain.User;
import com.lowang.ormquerydsl.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserWebFluxController {

  @Autowired UserService s;

  @GetMapping("/{id}")
  public Mono<User> findById(@PathVariable Long id) {
    return s.findById(id);
  }

  @PostMapping
  public Mono<User> save(User user) {
    return s.save(user);
  }

  @GetMapping
  public Flux<User> list(User user) {
    return s.list(user);
  }
}
