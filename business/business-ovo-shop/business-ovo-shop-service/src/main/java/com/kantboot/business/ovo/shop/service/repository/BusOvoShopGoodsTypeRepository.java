package com.kantboot.business.ovo.shop.service.repository;

import com.kantboot.business.ovo.shop.module.entity.BusOvoShopGoodsType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Ovo商城商品分类表
 * @author 方某方
 */
public interface BusOvoShopGoodsTypeRepository
extends JpaRepository<BusOvoShopGoodsType, Long>
{
    /**
     * 获取商品分类列表，平且根据优先级倒序
     * @return 商品分类列表
     */
     List<BusOvoShopGoodsType> findAllByOrderByPriorityDesc();
}
