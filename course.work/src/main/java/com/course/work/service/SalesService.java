package com.course.work.service;

import com.course.work.entity.Sales;

import java.util.List;
import java.util.Optional;

public interface SalesService {
    List<Sales> listSales();
    Optional<Sales> getById(Long id);
    Sales addSale(Sales sales);
    void deleteSale(Long id);
}
