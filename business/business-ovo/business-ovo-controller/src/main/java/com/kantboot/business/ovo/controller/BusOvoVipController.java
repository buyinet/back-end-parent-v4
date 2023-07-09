package com.kantboot.business.ovo.controller;

import com.kantboot.business.ovo.service.service.IBusOvoVipService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business/ovo/vip")
public class BusOvoVipController {

    @Resource
    private IBusOvoVipService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 获取会员列表
     * @return 会员列表
     */
    @RequestMapping("/getAll")
    public RestResult getAll(){
        return stateSuccessService.success(
                service.getAll(),
                "getSuccess"
        );
    }

}
