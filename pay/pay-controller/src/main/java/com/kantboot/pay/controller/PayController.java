package com.kantboot.pay.controller;

import com.kantboot.pay.service.service.IPayService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay/pay")
public class PayController {

    @Resource
    private IPayService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    @RequestMapping("/generatePayOrder")
    public RestResult generatePayOrder(String productCode,Double amount,String description){
        return stateSuccessService.success(service.generatePayOrder(productCode,amount,description,"CNY"),"getSuccess");
    }

    @RequestMapping("/wechatPay")
    public RestResult wechatPay(String orderId, String sceneCode,String code){
        return stateSuccessService.success(service.wechatPay(orderId,sceneCode,code),"getSuccess");
    }


}
