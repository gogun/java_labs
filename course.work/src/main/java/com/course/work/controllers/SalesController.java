package com.course.work.controllers;

import com.course.work.entity.Sales;
import com.course.work.exception.SaleNotFoundException;
import com.course.work.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {
    private SalesService salesService;

    public SalesController(@Autowired SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Sales>> getAllSales() {
        return new ResponseEntity<>(salesService.listSales(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sales> getSaleById(@PathVariable Long id) {
        return new ResponseEntity<>(salesService.getById(id)
        .orElseThrow(()-> new SaleNotFoundException(id)), HttpStatus.OK);
    }

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public Sales addGood(@RequestBody Sales sales) {
        return salesService.addSale(sales);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteGood(@PathVariable Long id) {
        salesService.deleteSale(id);
    }

}
