package com.kantboot.business.ovo.controller;

import com.kantboot.business.ovo.module.dto.BusOvoUserBindDto;
import com.kantboot.business.ovo.module.entity.BusOvoUserBind;
import com.kantboot.business.ovo.service.service.IBusOvoUserBindService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ovo用户绑定表控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/business/ovo/userBind")
public class BusOvoUserBindController {

    @Resource
    private IBusOvoUserBindService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 绑定用户
     * @return 绑定用户
     */
    @RequestMapping("/bind")
    public RestResult bind(@RequestBody BusOvoUserBindDto dto){
        return stateSuccessService.success(service.bind(dto), "optSuccess");
    }

    /**
     * 获取自己的信息
     * @return 自己的信息
     */
    @RequestMapping("/getSelf")
    public RestResult getSelf(){
        return stateSuccessService.success(service.getSelf(), "getSuccess");
    }

}
