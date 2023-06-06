package com.kantboot.system.controller;

import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.system.service.ISysBalanceService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 余额的控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/system/balance")
public class SysBalanceController {

    @Resource
    private ISysBalanceService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 获取自身的余额
     * @return 余额
     */
    @RequestMapping("/getSelfMap")
    public RestResult<Map<String,Double>> getSelfMap(){
        return stateSuccessService.success(service.getSelfMap(),"getSuccess");
    }


}
