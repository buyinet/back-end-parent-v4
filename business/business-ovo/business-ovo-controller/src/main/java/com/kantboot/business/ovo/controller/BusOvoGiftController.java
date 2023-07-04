package com.kantboot.business.ovo.controller;

import com.kantboot.business.ovo.service.service.IBusOvoGiftService;
import com.kantboot.system.service.IStateSuccessService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 礼物表控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/business/ovo/gift")
public class BusOvoGiftController {

    @Resource
    IStateSuccessService stateSuccessService;

    @Resource
    IBusOvoGiftService service;

    /**
     * 根据礼物编码获取礼物
     * @param code 礼物编码
     * @return 礼物
     */
    @RequestMapping("/getByCode")
    public Object getByCode(String code){
        return stateSuccessService.success(
                service.getByCode(code),
                "getSuccess"
        );
    }


    /**
     * 获取所有礼物
     * @return 所有礼物
     */
    @RequestMapping("/getAll")
    public Object getAll(){
        return stateSuccessService.success(
                service.getAll(),
                "getSuccess"
        );
    }


}
