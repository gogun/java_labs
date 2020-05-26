package com.course.work.service;

import com.course.work.entity.Goods;
import com.course.work.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GoodsServiceImpl implements GoodsService {

    private GoodsRepository goodsRepository;

    GoodsServiceImpl(@Autowired GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    @Override
    public List<Goods> listGoods() {
        return goodsRepository.findAll();
    }

    @Override
    public Goods addGood(Goods goods) {
        return goodsRepository.save(goods);
    }

    @Override
    public void deleteGood(Long id) {
        goodsRepository.deleteById(id);
    }

    @Override
    public Optional<Goods> findById(Long id) {
        return goodsRepository.findById(id);
    }
}
