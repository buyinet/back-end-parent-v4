package com.kantboot.system.controller;

import com.kantboot.system.module.dto.SecurityLoginAndRegisterDTO;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController {

    @Resource
    private ISysUserService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    @RequestMapping("/register")
    public RestResult register(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        // 调用service层的注册方法，返回结果，并返回成功信息
        return stateSuccessService.success(service.register(username, password), "registerSuccess");
    }

    @RequestMapping("/securityRegister")
    public RestResult securityRegister(
            @RequestBody SecurityLoginAndRegisterDTO dto) {
        // 调用service层的安全注册方法，返回结果，并返回成功信息
        return stateSuccessService.success(service.securityRegister(dto), "registerSuccess");
    }

    @RequestMapping("/login")
    public RestResult login(
            @RequestParam("account") String account,
            @RequestParam("password") String password) {
        // 调用service层的登录方法，返回结果，并返回成功信息
        return stateSuccessService.success(service.login(account, password), "loginSuccess");
    }

    @RequestMapping("/securityLogin")
    public RestResult securityLogin(
            @RequestBody SecurityLoginAndRegisterDTO dto) {
        // 调用service层的安全登录方法，返回结果，并返回成功信息
        return stateSuccessService.success(service.securityLogin(dto), "loginSuccess");
    }

}
