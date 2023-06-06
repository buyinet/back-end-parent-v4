package com.kantboot.business.ovo.controller;

import com.kantboot.business.ovo.service.service.IBusOvoSexualOrientationService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 性取向表控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/business/ovo/sexualOrientation")
public class BusOvoSexualOrientationController {

    @Resource
    private IBusOvoSexualOrientationService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 获取所有的性取向
     * @return 性取向
     */
    @RequestMapping("/getList")
    public RestResult getList(){
        return stateSuccessService.success(service.getAllSexualOrientation(), "getAllSuccess");
    }

    /**
     * 获取所有的性取向
     * @return 性取向
     */
    @RequestMapping("/getMap")
    public RestResult getMap(){
        return stateSuccessService.success(service.getMap(), "getSuccess");
    }


}
