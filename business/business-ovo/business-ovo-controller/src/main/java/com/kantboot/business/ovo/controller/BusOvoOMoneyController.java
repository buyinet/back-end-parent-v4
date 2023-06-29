package com.kantboot.business.ovo.controller;

import com.kantboot.business.ovo.service.service.IBusOvoOMoneyService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * O币控制器类
 * @author 方某方
 */
@RestController
@RequestMapping("/business/ovo/oMoney")
public class BusOvoOMoneyController {

    @Resource
    private IBusOvoOMoneyService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 获取O币列表
     * @return O币列表
     */
    @RequestMapping("/getOvoOMoneyList")
    public RestResult getOvoOMoneyList(){
        return stateSuccessService.success(service.getOvoOMoneyList(),"getSuccess");
    }

    /**
     * 购买O币
     * @return 订单号
     */
    @RequestMapping("/buy")
    public RestResult buy(Long id){
        return stateSuccessService.success(service.buy(id),"buySuccess");
    }



}
