package com.lowang.ormquerydsl.service;

import com.lowang.ormquerydsl.domain.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

  Mono<User> findById(String id);

  Mono<User> save(User user);

  Flux<User> list(User user);

  Mono<User> delete(String id);
}
