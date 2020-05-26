package com.course.work.service;

import com.course.work.entity.Goods;

import java.util.List;
import java.util.Optional;

public interface GoodsService {
    List<Goods> listGoods();
    Goods addGood(Goods goods);
    void deleteGood(Long id);
    Optional<Goods> findById(Long id);
}
