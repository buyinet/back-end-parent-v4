package com.kantboot.system.module.dto;

import lombok.Data;

/**
 * 安全登录和注册的DTO
 * @author 方某方
 */
@Data
public class SecurityLoginAndRegisterDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 加密过的密码
     */
    private String password;

    /**
     * 账号的公钥
     */
    private String publicKeyOfUsername;

    /**
     * 密码的公钥
     */
    private String publicKeyOfPassword;

}




