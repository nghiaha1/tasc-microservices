package com.tasc.user.repository;

import com.tasc.entity.BaseStatus;
import com.tasc.user.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    Page<Role> findByStatus(BaseStatus status, Pageable pageable);

    List<Role> findByIdIn(List<Long> ids);

}
