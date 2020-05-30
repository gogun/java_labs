package com.course.work.controllers;

import com.course.work.entity.Goods;
import com.course.work.entity.WarehouseOne;
import com.course.work.entity.WarehouseTwo;
import com.course.work.service.WarehouseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {
    private WarehouseService warehouseService;

    public WarehouseController(@Autowired WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @Data
    @AllArgsConstructor
    private static class Pair {
        private UUID uuid;
        private Integer amount;
    }


    @PostMapping("/left")
    public ResponseEntity<List<Pair>> getGoodsLeft(@RequestBody List<Goods> goods) {
        List<Pair> result = new LinkedList<>();
        for (Goods good : goods) {
            int size = result.size();
            warehouseService.findByGoodsIdOne(good.getId()).ifPresent((found) -> {
                result.add(new Pair(found.getId(), found.getCount()));
            });

            warehouseService.findByGoodsIdTwo(good.getId()).ifPresent((found) -> {
                result.add(new Pair(found.getId(), found.getCount()));
            });
            if (size == result.size()) {
                result.add(new Pair(null, 0));
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<WarehouseOne>> getWarehouseOneList() {
        List<WarehouseOne> warehouseOneList = warehouseService.listWarehouseOne();
        for (WarehouseTwo elem : warehouseService.listWarehouseTwo()) {
            warehouseOneList.add(new WarehouseOne(elem.getId(), elem.getGoods(), elem.getCount()));
        }
        return new ResponseEntity<>(warehouseOneList, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<WarehouseOne> addToWarehouse(@RequestBody WarehouseOne warehouseOne) {
        if (warehouseService.sizeOne() > warehouseService.sizeTwo()) {
            warehouseService.addGoodToTwo(new WarehouseTwo(warehouseOne.getId(),
                    warehouseOne.getGoods(), warehouseOne.getCount()));
        } else {
            warehouseService.addGoodToOne(warehouseOne);
        }

        return new ResponseEntity<>(warehouseOne, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<WarehouseOne> updateWarehouse(@RequestBody WarehouseOne warehouseOne) {

        if (warehouseOne.getId() == null) {
            return addToWarehouse(warehouseOne);
        }
        warehouseService.findInOneById(warehouseOne.getId()).ifPresent((res) -> {
            warehouseService.addGoodToOne(warehouseOne);
        });
        warehouseService.findInTwoById(warehouseOne.getId()).ifPresent((res) -> {
            warehouseService.addGoodToTwo(new WarehouseTwo(warehouseOne.getId(), warehouseOne.getGoods(),
                    warehouseOne.getCount()));
        });

        return new ResponseEntity<>(warehouseOne, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public void deleteFromWarehouse(@PathVariable UUID id, @RequestParam Integer count) {
        warehouseService.findInOneById(id).ifPresent((wh) -> {
            updateWarehouse(new WarehouseOne(wh.getId(), wh.getGoods(), wh.getCount() - count));
        });

        warehouseService.findInTwoById(id).ifPresent((wh) -> {
            updateWarehouse(new WarehouseOne(wh.getId(), wh.getGoods(), wh.getCount() - count));
        });
    }
}
