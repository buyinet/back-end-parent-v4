package com.kantboot.system.controller;

import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.repository.SysUserRepository;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController {

    @Resource
    private SysUserRepository sysUserRepository;


}
