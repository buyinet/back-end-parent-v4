package com.kantboot.system.controller;

import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.system.service.ISysBalanceTypeService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 余额类型的控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/system/balanceType")
public class SysBalanceTypeController {

    @Resource
    private ISysBalanceTypeService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 获取余额类型的map
     * @return map
     */
    @RequestMapping("/getMap")
    public RestResult getMap(){
        return stateSuccessService.success(service.getMap(),"getSuccess");
    }

}
