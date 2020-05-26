package com.course.work.service;

import com.course.work.entity.Goods;
import com.course.work.entity.WarehouseOne;
import com.course.work.entity.WarehouseTwo;
import com.course.work.repository.SalesRepository;
import com.course.work.repository.WarehouseOneRepository;
import com.course.work.repository.WarehouseTwoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private WarehouseOneRepository warehouseOneRepository;
    private WarehouseTwoRepository warehouseTwoRepository;

    WarehouseServiceImpl(@Autowired WarehouseOneRepository warehouseOneRepository,
                     @Autowired WarehouseTwoRepository warehouseTwoRepository) {
        this.warehouseOneRepository = warehouseOneRepository;
        this.warehouseTwoRepository = warehouseTwoRepository;
    }

    @Override
    public List<WarehouseOne> listWarehouseOne() {
        return warehouseOneRepository.findAll();
    }

    @Override
    public List<WarehouseTwo> listWarehouseTwo() {
        return warehouseTwoRepository.findAll();
    }

    @Override
    public void addGoodToOne(WarehouseOne warehouseOne) {
        warehouseOneRepository.save(warehouseOne);
    }

    @Override
    public void addGoodToTwo(WarehouseTwo warehouseTwo)  {
        warehouseTwoRepository.save(warehouseTwo);
    }

    @Override
    public Optional<WarehouseOne> findInOneById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<WarehouseTwo> findInTwoById(Long id) {
        return Optional.empty();
    }

    @Override
    public Long sizeOne() {
        return warehouseOneRepository.count();
    }

    @Override
    public Long sizeTwo() {
        return warehouseTwoRepository.count();
    }
}
