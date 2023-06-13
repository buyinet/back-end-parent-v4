package com.kantboot.system.controller;

import com.kantboot.system.module.dto.SecurityLoginAndRegisterDTO;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.repository.SysUserRepository;
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

    /**
     * 获取自己的信息
     * @return 自己的信息
     */
    @RequestMapping("/getSelf")
    public RestResult getSelf() {
        // 调用service层的获取自己信息方法，返回结果，并返回成功信息
        return stateSuccessService.success(service.getSelf(), "getSuccess");
    }

    /**
     * 修改头像
     * @param fileId 头像文件id
     * @return 修改后的用户信息
     */
    @RequestMapping("/updateAvatar")
    public RestResult updateAvatar(@RequestParam("fileId") Long fileId) {
        // 调用service层的修改头像方法，返回结果，并返回成功信息
        return stateSuccessService.success(service.updateAvatar(fileId), "saveSuccess");
    }

}
