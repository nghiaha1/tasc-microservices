package com.tasc.user.repository;

import com.tasc.user.entity.Method;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MethodRepository extends JpaRepository<Method, Long> {

    List<Method> findByIdIn(List<Long> ids);
}
