package com.lowang.ormquerydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lowang.ormquerydsl.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {}
