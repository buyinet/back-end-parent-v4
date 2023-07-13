package com.kantboot.admin.business.ovo.shop.controller;


import com.kantboot.base.controller.BaseAdminController;
import com.kantboot.business.ovo.shop.module.entity.BusOvoShopGoods;
import com.kantboot.business.ovo.shop.module.entity.BusOvoShopGoodsType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/adminManage/business/ovoShop/goods")
public class AdminManageBussinessShopGoodsController
extends BaseAdminController<BusOvoShopGoods,Long>
{

}
