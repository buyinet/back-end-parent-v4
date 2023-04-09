package com.kantboot.system.service;

import com.kantboot.system.module.entity.SysToken;

/**
 * token服务接口
 * @author 方某方
 */
public interface ISysTokenService {

    /**
     * 生成token
     * @param userId 用户id
     * @return token
     */
    SysToken createToken(Long userId);

}
