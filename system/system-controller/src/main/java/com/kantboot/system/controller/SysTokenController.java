package com.kantboot.system.controller;

import com.kantboot.system.module.entity.SysToken;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.system.service.ISysTokenService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统token的控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/system/token")
public class SysTokenController {

    @Resource
    private ISysTokenService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    @RequestMapping("/create")
    public RestResult<SysToken> create(@RequestParam("userId") Long userId){
        // 生成token，并告知前端创建成功
        return stateSuccessService.success(service.createToken(userId), "createSuccess");
    }
}
