package com.kantboot.system.service;

import com.kantboot.system.module.entity.SysToken;

/**
 * token服务接口
 * @author 方某方
 */
public interface ISysTokenService {

    /**
     * 隐藏敏感信息
     * @param token token信息
     *              会修改传入的对象
     * @return token信息
     */
    SysToken hideSensitiveInfo(SysToken token);

    /**
     * 生成token
     * @param userId 用户id
     * @return token
     */
    SysToken createToken(Long userId);


    /**
     * 根据token刷新token
     * @param token token
     * @return token
     */
    SysToken refreshToken(String token);

    /**
     * 刷新token
     * @return token
     */
    SysToken refreshToken();


    /**
     * 根据用户id获取token信息
     * @return token信息
     */
    SysToken getSelf();


}
