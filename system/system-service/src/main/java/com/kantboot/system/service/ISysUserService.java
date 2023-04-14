package com.kantboot.system.service;

import com.kantboot.system.module.dto.SecurityLoginAndRegisterDTO;
import com.kantboot.system.module.entity.SysToken;
import com.kantboot.system.module.entity.SysUser;

/**
 * 系统用户服务接口
 * @author 方某方
 */
public interface ISysUserService {

    /**
     * 隐藏敏感信息
     * @param user 用户信息
     *             会修改传入的对象
     * @return 用户信息
     */
    SysUser hideSensitiveInfo(SysUser user);

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
     *            即加密过的账号密码
     *            以及账号密码的公钥
     * @return token
     */
    SysToken securityRegister(SecurityLoginAndRegisterDTO dto);

    /**
     * 登录（普通登录）
     * @param account 账号
     * @param password 密码
     * @return token
     */
    SysToken login(String account, String password);

    /**
     * 登录（安全登录）
     * @param dto 登录信息
     *            即加密过的账号密码
     *            以及账号密码的公钥
     * @return token
     */
    SysToken securityLogin(SecurityLoginAndRegisterDTO dto);

    /**
     * 根据用户Id获取用户信息
     * @param id 用户Id
     * @return 用户信息
     */
    SysUser getById(Long id);

    /**
     * 获取用户自身的信息
     * @return 用户信息
     */
    SysUser getSelf();

    /**
     * 获取用户自身的id
     * @return 用户id
     */
    Long getIdOfSelf();

    /**
     * 第三方注册，用于第三方接口调用
     * @param user 用户信息
     *             返回给客户端时，会隐藏敏感信息
     * @return token
     */
    SysToken thirdRegister(SysUser user);


    /**
     * 第三方登录，用于第三方接口调用
     * @param userId 用户信息
     *             返回给客户端时，会隐藏敏感信息
     * @return token
     */
    SysToken thirdLogin(Long userId);


}
