package com.kantboot.system.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.kantboot.system.module.entity.SysToken;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.repository.SysTokenRepository;
import com.kantboot.system.repository.SysUserRepository;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.system.service.ISysSettingService;
import com.kantboot.system.service.ISysTokenService;
import com.kantboot.util.common.exception.BaseException;
import com.kantboot.util.common.http.HttpRequestHeaderUtil;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    @Resource
    private ISysExceptionService exceptionService;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public SysToken hideSensitiveInfo(SysToken token) {
        // 隐藏UserAgent
        token.setCreateUserAgent(null);
        // 隐藏创建时的ip
        token.setCreateIp(null);
        // 隐藏创建时的设备
        token.setCreateDevice(null);
        // 隐藏最后一次使用的ip
        token.setLastIp(null);
        // 隐藏场景
        token.setSceneCode(null);
        // 隐藏过期时间
        token.setGmtExpire(null);
        // 隐藏创建时间
        token.setGmtCreate(null);
        // 隐藏更新时间
        token.setGmtModified(null);

        return token;
    }

    @Override
    public SysToken createToken(Long userId) {

        // 获取token过期时间
        String tokenExpireTimeStr = settingService.getValue("user", "tokenExpireTime");
        long tokenExpireTime = Long.parseLong(tokenExpireTimeStr);

        // 过期时间
        long expireTime = System.currentTimeMillis() + tokenExpireTime;

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
        SysUser sysUser = userRepository.findById(userId).orElseThrow(()->exceptionService.getException("userNotExist"));

        // 保存token，并返回告知用户
        return hideSensitiveInfo(repository.save(token).setUser(sysUser));
    }


    @Override
    public SysToken refreshToken(String token) {
        // redis中的key
        String redisKey = "token:" + token;
        // 如果redis中有token，说明已经刷新过了，直接返回
        if (redisUtil.hasKey(redisKey)) {
            return JSON.parseObject(redisUtil.get(redisKey), SysToken.class);
        }

        // 根据token获取token信息
        SysToken byToken = repository.findByToken(token);
        // 如果token信息为空，则返回null
        if (null==byToken){
            throw exceptionService.getException("tokenNotExist");
        }

        // 如果token已过期，返回token过期异常
        if (byToken.getGmtExpire().getTime()<System.currentTimeMillis()){
            repository.delete(byToken);
            throw exceptionService.getException("tokenExpire");
        }

        // 获取token过期时间
        String tokenExpireTimeStr = settingService.getValue("user", "tokenExpireTime");
        long tokenExpireTime = Long.parseLong(tokenExpireTimeStr);
        // 过期时间
        long expireTime = System.currentTimeMillis() + tokenExpireTime;

        // 设置过期时间
        byToken.setGmtExpire(new Date(expireTime));
        // 设置最后一次使用的ip
        byToken.setLastIp(httpRequestHeaderUtil.getIp());
        // 保存token
        repository.save(byToken);

        // 获取token信息
        SysToken tokenInfo = repository.findByToken(token);

        // 保存token到redis，过期时间为30分钟
        redisUtil.setEx(redisKey, JSON.toJSONString(tokenInfo), 30, TimeUnit.MINUTES);

        // 返回token信息
        return tokenInfo;
    }


    @Override
    public SysToken refreshToken() {
        try {
            // 获取token
            SysToken token = refreshToken(httpRequestHeaderUtil.getToken());
            return token;
        } catch (BaseException e) {
            String tokenNotExist = "tokenNotExist";
            if(tokenNotExist.equals(e.getStateCode())){
                throw exceptionService.getException("notLogin");
            }
            String tokenExpire = "tokenExpire";
            if (tokenExpire.equals(e.getStateCode())){
                // 如果token过期，返回登录状态过期异常
                throw exceptionService.getException("loginStateExpired");
            }
            throw exceptionService.getException("notLogin");
        }

    }

    @Override
    public SysToken getSelf() {
        // 刷新token，并获取token信息
        SysToken byToken = refreshToken();
        return hideSensitiveInfo(byToken);
    }
}
