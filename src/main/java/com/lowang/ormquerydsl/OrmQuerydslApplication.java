package com.lowang.ormquerydsl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.querydsl.jpa.impl.JPAQueryFactory;

import io.shardingsphere.core.keygen.DefaultKeyGenerator;
import io.shardingsphere.core.keygen.KeyGenerator;

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

  @Bean
  public KeyGenerator keyGenerator() {
    InetAddress address;
    try {
      address = InetAddress.getLocalHost();
    } catch (final UnknownHostException e) {
      throw new IllegalStateException(
          "Cannot get LocalHost InetAddress, please check your network!");
    }
    // 得到IP地址的byte[]形式值
    byte[] ipAddressByteArray = address.getAddress();
    long workerId = 0L;
    // 如果是IPV4，计算方式是遍历byte[]，然后把每个IP段数值相加得到的结果就是workerId
    if (ipAddressByteArray.length == 4) {
      for (byte byteNum : ipAddressByteArray) {
        workerId += byteNum & 0xFF;
      }
      // 如果是IPV6，计算方式是遍历byte[]，然后把每个IP段后6位（& 0B111111 就是得到后6位）数值相加得到的结果就是workerId
    } else if (ipAddressByteArray.length == 16) {
      for (byte byteNum : ipAddressByteArray) {
        workerId += byteNum & 0B111111;
      }
    } else {
      throw new IllegalStateException("Bad LocalHost InetAddress, please check your network!");
    }
    DefaultKeyGenerator.setWorkerId(workerId);
    return new DefaultKeyGenerator();
  }
}
