package com.course.work.repository;

import com.course.work.entity.WarehouseOne;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WarehouseOneRepository extends JpaRepository<WarehouseOne, Long> {
    Optional<WarehouseOne> findById(UUID id);
    Optional<WarehouseOne> findByGoodsId(Long id);
}

