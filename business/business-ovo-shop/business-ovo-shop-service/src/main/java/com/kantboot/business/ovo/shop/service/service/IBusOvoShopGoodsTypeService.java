package com.kantboot.business.ovo.shop.service.service;

import com.kantboot.business.ovo.shop.module.entity.BusOvoShopGoodsType;

import java.util.List;

/**
 * Ovo商城商品分类表
 * @author 方某某
 */
public interface IBusOvoShopGoodsTypeService {

    /**
     * 获取商品分类列表
     * @return 商品分类列表
     */
    List<BusOvoShopGoodsType> getList();

}
