package com.kantboot.business.ovo.shop.service.service;

import com.kantboot.business.ovo.shop.module.entity.BusOvoShopGoods;
import com.kantboot.util.common.result.PageResult;
import org.springframework.data.domain.Page;

/**
 * Ovo商城商品表
 * 用于处理商品的相关请求
 * @author 方某方
 */
public interface IBusOvoShopGoodsService {

    PageResult getByTypeCode(String typeCode, Integer pageNumber);

    /**
     * 根据商品id获取商品详情
     * @param id 商品id
     * @return 商品详情
     */
    BusOvoShopGoods getById(Long id);

}
