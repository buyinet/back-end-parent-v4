package com.kantboot.system.service.impl;

import cn.hutool.core.util.IdUtil;
import com.kantboot.system.module.entity.SysToken;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.repository.SysTokenRepository;
import com.kantboot.system.repository.SysUserRepository;
import com.kantboot.system.service.ISysSettingService;
import com.kantboot.system.service.ISysTokenService;
import com.kantboot.util.common.http.HttpRequestHeaderUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * token服务实现类
 * @author 方某方
 */
@Service
public class SysTokenServiceImpl implements ISysTokenService {

    @Resource
    private SysTokenRepository repository;

    @Resource
    private ISysSettingService settingService;

    @Resource
    private SysUserRepository userRepository;

    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil;



    @Override
    public SysToken createToken(Long userId) {

        // 获取token过期时间
        String tokenExpireTimeStr = settingService.getValue("user", "tokenExpireTime");
        Long tokenExpireTime = Long.parseLong(tokenExpireTimeStr);

        // 过期时间
        Long expireTime = System.currentTimeMillis() + tokenExpireTime;
        // 生成token
        SysToken token = new SysToken();
        // 设置过期时间
        token.setGmtExpire(new Date(expireTime));
        // 设置用户id
        token.setUserId(userId);
        // 设置token
        token.setToken(IdUtil.simpleUUID());
        // 设置userId
        token.setUserId(userId);
        // 设置用户UserAgent
        token.setCreateUserAgent(httpRequestHeaderUtil.getUserAgent());
        // 设置用户ip
        token.setCreateIp(httpRequestHeaderUtil.getIp());
        // 设置用户设备
        token.setCreateDevice(httpRequestHeaderUtil.getDevice());
        // 设置用户场景
        token.setSceneCode(httpRequestHeaderUtil.getSceneCode());
        // 设置用户Ip
        token.setLastIp(httpRequestHeaderUtil.getIp());
        // 根据用户id查询用户
        SysUser sysUser = userRepository.findById(userId).get();

        // 保存token，并返回告知用户
        return repository.save(token).setUser(sysUser);
    }

}
