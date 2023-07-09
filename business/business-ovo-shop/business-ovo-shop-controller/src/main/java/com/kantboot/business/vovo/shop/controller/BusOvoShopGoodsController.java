package com.kantboot.business.vovo.shop.controller;

import com.kantboot.business.ovo.shop.service.service.IBusOvoShopGoodsService;
import com.kantboot.system.service.IStateSuccessService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business/ovoShop/goods")
public class BusOvoShopGoodsController {

    @Resource
    private IBusOvoShopGoodsService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    @RequestMapping("/getByTypeCode")
    public Object getByTypeCode(String typeCode, Integer pageNumber) {
        return stateSuccessService.success(service.getByTypeCode(typeCode, pageNumber),"getSuccess");
    }

}
