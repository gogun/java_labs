package com.course.work.service;

import com.course.work.entity.Goods;
import com.course.work.entity.WarehouseOne;
import com.course.work.entity.WarehouseTwo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WarehouseService {
    List<WarehouseOne> listWarehouseOne();

    List<WarehouseTwo> listWarehouseTwo();

    void addGoodToOne(WarehouseOne warehouseOne);

    void addGoodToTwo(WarehouseTwo warehouseTwo);

    Optional<WarehouseOne> findInOneById(UUID id);

    Optional<WarehouseTwo> findInTwoById(UUID id);

    Optional<WarehouseOne> findByGoodsIdOne(Long id);

    Optional<WarehouseTwo> findByGoodsIdTwo(Long id);

    Long sizeOne();

    Long sizeTwo();
}
