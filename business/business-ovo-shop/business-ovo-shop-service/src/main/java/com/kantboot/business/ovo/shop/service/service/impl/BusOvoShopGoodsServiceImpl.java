package com.kantboot.business.ovo.shop.service.service.impl;

import com.kantboot.business.ovo.shop.module.entity.BusOvoShopGoods;
import com.kantboot.business.ovo.shop.service.repository.BusOvoShopGoodsRepository;
import com.kantboot.business.ovo.shop.service.service.IBusOvoShopGoodsService;
import com.kantboot.util.common.result.PageResult;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Ovo商城商品表
 * @author 方某方
 */
@Service
public class BusOvoShopGoodsServiceImpl implements IBusOvoShopGoodsService {

    @Resource
    private BusOvoShopGoodsRepository repository;

    @Override
    public PageResult getByTypeCode(String typeCode, Integer pageNumber) {
        return
                PageResult.of(
        repository.findAllByTypeCode(typeCode,
                Pageable.ofSize(20)
                        .withPage(pageNumber - 1)
                ));
    }

    @Override
    public BusOvoShopGoods getById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
