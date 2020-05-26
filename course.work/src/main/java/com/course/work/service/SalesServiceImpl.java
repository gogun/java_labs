package com.course.work.service;

import com.course.work.entity.Sales;
import com.course.work.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalesServiceImpl implements SalesService {

    private SalesRepository salesRepository;

    SalesServiceImpl(@Autowired SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    @Override
    public List<Sales> listSales() {
        return salesRepository.findAll();
    }

    @Override
    public Optional<Sales> getById(Long id) {
        return salesRepository.findById(id);
    }

    @Override
    public Sales addSale(Sales sales) {
        return salesRepository.save(sales);
    }

    @Override
    public void deleteSale(Long id) {
        salesRepository.deleteById(id);
    }
}
