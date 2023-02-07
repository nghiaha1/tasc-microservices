package com.tasc.user.repository;

import com.tasc.user.entity.Uri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UriRepository extends JpaRepository<Uri, Long> {
    List<Uri> findByIdIn(List<Long> ids);
}
