package com.lowang.ormquerydsl;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.querydsl.jpa.impl.JPAQueryFactory;

@SpringBootApplication
public class OrmQuerydslApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrmQuerydslApplication.class, args);
  }

  @Bean
  @Autowired
  public JPAQueryFactory jpaQuery(EntityManager entityManager) {
    return new JPAQueryFactory(entityManager);
  }
}
