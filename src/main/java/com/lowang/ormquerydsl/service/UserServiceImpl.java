package com.lowang.ormquerydsl.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowang.ormquerydsl.domain.QUser;
import com.lowang.ormquerydsl.domain.User;
import com.lowang.ormquerydsl.repository.UserRepository;
import com.lowang.ormquerydsl.util.Sequence;
import com.querydsl.jpa.impl.JPAQueryFactory;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
  @Autowired JPAQueryFactory q;
  @Autowired UserRepository r;

  @Override
  public Mono<User> findById(Long id) {
    return Mono.create(
        cb ->
            cb.success(
                q.select(QUser.user).from(QUser.user).where(QUser.user.id.eq(id)).fetchOne()));
  }

  @Override
  public Mono<User> save(User user) {
    user.setId(Sequence.get().nextId());
    return Mono.create(cb -> cb.success(r.save(user)));
  }

  @Override
  public Flux<User> list(User user) {
    return Flux.fromIterable(q.select(QUser.user).from(QUser.user).fetch());
  }

  @Override
  public Mono<User> delete(Long id) {
    return Mono.create(
        cb -> {
          Optional<User> u = r.findById(id);
          r.deleteById(id);
          cb.success(u.get());
        });
  }
}
