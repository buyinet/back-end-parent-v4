package com.kantboot.system.controller;

import com.kantboot.system.service.IRsaService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RSA控制器
 * 用于前端获取公钥
 * @author 方某方
 */
@CrossOrigin
@RestController
@RequestMapping("/system/rsa")
public class RsaController {

    @Resource
    private IRsaService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 生成公钥
     * 用于前端获取公钥
     * @return 公钥
     */
    @RequestMapping("/createPublicKey")
    public RestResult<String> createPublicKey() {
        // 调用service层的获取公钥方法，返回结果，并返回成功信息
        return stateSuccessService.success(service.createPublicKey(), "getSuccess");
    }


}
