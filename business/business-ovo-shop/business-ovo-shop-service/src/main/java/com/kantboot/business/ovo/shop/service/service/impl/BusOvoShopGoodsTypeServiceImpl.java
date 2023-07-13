package com.kantboot.business.ovo.shop.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kantboot.business.ovo.shop.module.entity.BusOvoShopGoodsType;
import com.kantboot.business.ovo.shop.service.repository.BusOvoShopGoodsTypeRepository;
import com.kantboot.business.ovo.shop.service.service.IBusOvoShopGoodsTypeService;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<String, String> getMap() {


        List<BusOvoShopGoodsType> allByOrderByPriorityDesc = repository.findAllByOrderByPriorityDesc();

        Map<String,String> result = new HashMap<>(10);

        allByOrderByPriorityDesc.forEach(item->{
            result.put(item.getCode(),item.getName());
        });

        return result;

    }

}
