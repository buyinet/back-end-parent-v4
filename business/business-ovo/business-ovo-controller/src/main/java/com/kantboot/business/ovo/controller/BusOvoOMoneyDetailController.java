package com.kantboot.business.ovo.controller;

import com.kantboot.business.ovo.service.service.IBusOvoOMoneyDetailService;
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
@RequestMapping("/business/ovo/oMoneyDetail")
public class BusOvoOMoneyDetailController {

    @Resource
    private IBusOvoOMoneyDetailService service;

    @Resource
    private IStateSuccessService stateSuccessService;


    /**
     * 获取O币明细
     */
    @RequestMapping("/getSelf")
    public RestResult getSelf(Integer pageNumber){
        return stateSuccessService.success(service.getSelf(pageNumber),"getSuccess");
    }


}
