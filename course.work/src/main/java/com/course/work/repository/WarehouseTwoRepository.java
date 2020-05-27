package com.course.work.repository;

import com.course.work.entity.WarehouseOne;
import com.course.work.entity.WarehouseTwo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WarehouseTwoRepository extends JpaRepository<WarehouseTwo, Long> {
    Optional<WarehouseTwo> findById(UUID id);
    Optional<WarehouseTwo> findByGoodsId(Long id);
}
