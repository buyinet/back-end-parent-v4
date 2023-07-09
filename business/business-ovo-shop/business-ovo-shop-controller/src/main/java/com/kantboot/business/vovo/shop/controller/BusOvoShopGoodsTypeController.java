package com.kantboot.business.vovo.shop.controller;

import com.kantboot.business.ovo.shop.service.service.IBusOvoShopGoodsTypeService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ovo商城商品分类的Controller
 * 用于处理商品分类的相关请求
 */
@RestController
@RequestMapping("/business/ovoShop/goodsType")
public class BusOvoShopGoodsTypeController {

    @Resource
    private IBusOvoShopGoodsTypeService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    @RequestMapping("/getList")
    public RestResult getList() {
        return stateSuccessService.success(service.getList(),"getSuccess");
    }

}
