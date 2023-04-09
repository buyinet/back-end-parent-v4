package com.kantboot.system.service.impl;

import com.kantboot.system.module.dto.SecurityLoginAndRegisterDTO;
import com.kantboot.system.module.entity.SysToken;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.repository.SysUserRepository;
import com.kantboot.system.service.ISysUserService;
import jakarta.annotation.Resource;

/**
 * 用户服务实现类
 * @author 方某方
 */
public class SysUserServiceImpl implements ISysUserService {

    @Resource
    private SysUserRepository repository;

    @Override
    public SysToken register(String username, String password) {
        // 1. 根据用户名查询用户
        SysUser byUsername = repository.findByUsername(username);
        // 2. 判断用户是否存在


        return null;
    }

    @Override
    public SysToken securityRegister(SecurityLoginAndRegisterDTO dto) {
        return null;
    }


}
