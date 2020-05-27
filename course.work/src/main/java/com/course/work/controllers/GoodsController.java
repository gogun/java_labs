package com.course.work.controllers;

import com.course.work.entity.Goods;
import com.course.work.exception.GoodNotFoundException;
import com.course.work.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {
    private GoodsService goodsService;

    public GoodsController(@Autowired GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Goods>> getAllGoods() {
        return new ResponseEntity<>(goodsService.listGoods(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Goods> getById(@PathVariable Long id) {
        return new ResponseEntity<>(goodsService.findById(id)
                .orElseThrow(() -> new GoodNotFoundException(id)), HttpStatus.OK);
    }

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public Goods addGood(@RequestBody Goods goods) {
        return goodsService.addGood(goods);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteGood(@PathVariable Long id) {
        goodsService.deleteGood(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Goods> updateGood(@PathVariable Long id, @RequestParam String name,
                           @RequestParam Float priority) {
        return new ResponseEntity<>(goodsService.findById(id)
                .map(good -> {
                    good.setName(name);
                    good.setPriority(priority);

                    return good;
                }).orElseThrow(() -> new GoodNotFoundException(id)), HttpStatus.OK);
    }

}
