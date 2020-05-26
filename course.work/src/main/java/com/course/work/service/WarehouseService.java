package com.course.work.service;

import com.course.work.entity.Goods;
import com.course.work.entity.WarehouseOne;
import com.course.work.entity.WarehouseTwo;

import java.util.List;
import java.util.Optional;

public interface WarehouseService {
    List<WarehouseOne> listWarehouseOne();

    List<WarehouseTwo> listWarehouseTwo();

    void addGoodToOne(WarehouseOne warehouseOne);

    void addGoodToTwo(WarehouseTwo warehouseTwo);

    Optional<WarehouseOne> findInOneById(Long id);

    Optional<WarehouseTwo> findInTwoById(Long id);

    Long sizeOne();

    Long sizeTwo();
}
