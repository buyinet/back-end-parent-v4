package com.kantboot.business.ovo.shop.service.repository;

import com.kantboot.business.ovo.shop.module.entity.BusOvoShopGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Ovo商城商品表
 * @author 方某方
 */
public interface BusOvoShopGoodsRepository
extends JpaRepository<BusOvoShopGoods, Long>
{

    /**
     * 根据商品分类编码获取商品列表
     * @param typeCode 商品分类编码
     * @param pageable 分页信息
     * @return
     */
    Page<BusOvoShopGoods> findAllByTypeCode(String typeCode, Pageable pageable);
}
