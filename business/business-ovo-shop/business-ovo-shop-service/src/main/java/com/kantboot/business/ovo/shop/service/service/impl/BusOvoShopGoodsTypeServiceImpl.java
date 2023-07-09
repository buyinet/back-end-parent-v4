package com.kantboot.business.ovo.shop.service.service.impl;

import com.kantboot.business.ovo.shop.module.entity.BusOvoShopGoodsType;
import com.kantboot.business.ovo.shop.service.repository.BusOvoShopGoodsTypeRepository;
import com.kantboot.business.ovo.shop.service.service.IBusOvoShopGoodsTypeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Ovo商城商品分类表
 * @author 方某方
 */
@Service
public class BusOvoShopGoodsTypeServiceImpl
implements IBusOvoShopGoodsTypeService
{

    @Resource
    private BusOvoShopGoodsTypeRepository repository;

    @Override
    public List<BusOvoShopGoodsType> getList() {
        return repository.findAllByOrderByPriorityDesc();
    }

}
