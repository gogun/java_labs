package com.course.work.controllers;

import com.course.work.entity.Goods;
import com.course.work.entity.WarehouseOne;
import com.course.work.entity.WarehouseTwo;
import com.course.work.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {
    private WarehouseService warehouseService;

    public WarehouseController(@Autowired WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/left/{goodId}")
    public ResponseEntity<Integer> getGoodsLeft(@PathVariable Long goodId) {
        AtomicInteger count = new AtomicInteger(0);
        warehouseService.listWarehouseOne().stream()
                .forEach((elem) -> {
                    if (elem.getGoods().getId().equals(goodId)) {
                        count.addAndGet(elem.getCount());
                    }
                });
        warehouseService.listWarehouseTwo().stream()
                .forEach((elem) -> {
                    if (elem.getGoods().getId().equals(goodId)) {
                        count.addAndGet(elem.getCount());
                    }
                });
        return new ResponseEntity<>(count.get(), HttpStatus.OK);
    }

    @GetMapping("/1")
    public ResponseEntity<List<WarehouseOne>> getWarehouseOneList() {
        return new ResponseEntity<>(warehouseService.listWarehouseOne(), HttpStatus.OK);
    }

    @GetMapping("/2")
    public ResponseEntity<List<WarehouseTwo>> getWarehouseTwoList() {
        return new ResponseEntity<>(warehouseService.listWarehouseTwo(), HttpStatus.OK);
    }

    @PostMapping("/add/1")
    public void addToWarehouse(@RequestBody WarehouseOne warehouseOne) {
        warehouseService.addGoodToOne(warehouseOne);
    }

    @PostMapping("/add/2")
    public void addToWarehouse(@RequestBody WarehouseTwo warehouseTwo) {
        warehouseService.addGoodToTwo(warehouseTwo);
    }
}
