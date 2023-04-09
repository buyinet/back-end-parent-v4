package com.kantboot.system.service;

import com.kantboot.system.module.dto.SecurityLoginAndRegisterDTO;
import com.kantboot.system.module.entity.SysToken;

/**
 * 系统用户服务接口
 * @author 方某方
 */
public interface ISysUserService {

    /**
     * 注册（普通注册）
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    SysToken register(String username, String password);

    /**
     * 注册（安全注册）
     * @param dto 注册信息
     * @return token
     */
    SysToken securityRegister(SecurityLoginAndRegisterDTO dto);

}
