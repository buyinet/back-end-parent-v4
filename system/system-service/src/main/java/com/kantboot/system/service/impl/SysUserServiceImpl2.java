package com.kantboot.system.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.kantboot.system.module.dto.SecurityLoginAndRegisterDTO;
import com.kantboot.system.module.entity.SysToken;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.repository.SysUserRepository;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.util.common.http.HttpRequestHeaderUtil;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 系统用户服务接口实现
 * 上一个版本的token，想着一改常态，先放进数据库，之后再改成redis也存一份
 * 但是写着写着便变得混乱无比，所以干脆就不要数据库了，直接redis，而这里得到的token也是redis的
 * 但为了兼容，这里还是返回SysToken
 *
 * @author 方某方
 */
@Service
public class SysUserServiceImpl2 implements ISysUserService {

    @Resource
    private SysUserRepository repository;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private HttpRequestHeaderUtil requestHeaderUtil;

    @Resource
    private ISysExceptionService exceptionService;

    /**
     * 对用户的手机号进行隐私保护，将中间4位数字用星号替代
     *
     * @param phone 用户手机号
     * @return 隐私保护后的手机号
     */
    private String protectPhoneNumber(String phone) {
        if (phone == null) {
            return null;
        }
        int length = phone.length();
        // 将手机号中间四位替换为星号
        return String.format("%s%s", phone.substring(0, length - 4).replace(".", "\\*"), phone.substring(length - 4));
    }

    /**
     * 对用户的邮箱进行隐私保护，将@前面的字符用星号替代
     *
     * @param email 用户邮箱
     * @return 隐私保护后的邮箱
     */
    private String protectEmail(String email) {
        if (email == null) {
            return null;
        }
        int index = email.indexOf("@");
        if (index == -1) {
            return email;
        }
        // 将邮箱@前面的字符替换为星号
        return String.format("%s%s", email.substring(0, index).replace(".", "\\*"), email.substring(index));
    }

    /**
     * 对用户的账号进行隐私保护，将账号的前三位和后三位用星号替代
     *
     * @param username 用户账号
     * @return 隐私保护后的账号
     */
    private String protectUsername(String username) {
        if (username == null) {
            return null;
        }
        int length = username.length();
        if (length <= 6) {
            return username;
        }
        // 将账号的前三位和后三位替换为星号
        return String.format("%s%s", username.substring(0, 3).replace(".", "\\*"), username.substring(length - 3));
    }

    /**
     * 隐藏敏感信息
     *
     * @param user 用户信息
     *             会修改传入的对象
     * @return
     */
    @Override
    public SysUser hideSensitiveInfo(SysUser user) {
        SysUser result = user;
        // 隐藏手机号
        result.setPhone(protectPhoneNumber(user.getPhone()));
        // 隐藏密码
        result.setPassword(null);
        // 隐藏邮箱
        result.setEmail(protectEmail(user.getEmail()));
        // 隐藏账号
        result.setUsername(protectUsername(user.getUsername()));
        return result;
    }

    @Override
    public SysToken register(String username, String password) {
        return null;
    }

    @Override
    public SysToken securityRegister(SecurityLoginAndRegisterDTO dto) {
        // 先用微信小程序，后面再加
        return null;
    }

    @Override
    public SysToken login(String account, String password) {
        // 先用微信小程序，后面再加
        return null;
    }

    @Override
    public SysToken securityLogin(SecurityLoginAndRegisterDTO dto) {
        // 先用微信小程序，后面再加
        return null;
    }

    @Override
    public SysUser getById(Long id) {
        String redisKey = "userId:" + id + ":SysUser";
        // 从redis中获取用户信息的json字符串
        String sysUserJsonStr = redisUtil.get(redisKey);
        if (sysUserJsonStr != null) {
            // 转换为SysUser对象
            SysUser sysUser = JSON.parseObject(sysUserJsonStr, SysUser.class);
            return hideSensitiveInfo(sysUser);
        }
        SysUser result = repository.findById(id).get();
        if (result == null) {
            throw exceptionService.getException("userNotExist");
        }
        // 将用户信息存入redis
        redisUtil.set(redisKey, JSON.toJSONString(result));
        return hideSensitiveInfo(result);
    }

    @Override
    public SysUser getSelf() {
        String token = requestHeaderUtil.getToken();

        if (token == null) {

            // 如果token为空，抛出未登录异常
            throw exceptionService.getException("notLogin");
        }

        String userId = redisUtil.get("token:" + token + ":userId");
        if (userId == null) {
            // 如果userId为空，抛出未登录异常
            throw exceptionService.getException("notLogin");
        }
        return getById(Long.parseLong(userId));
    }

    @Override
    public Long getIdOfSelf() {
        return getSelf().getId();
    }

    @Override
    public SysToken thirdRegister(SysUser user) {
        // 生成token字符串
        String token = IdUtil.simpleUUID();
        // 将token存入redis
        SysUser save = repository.save(user);
        redisUtil.setEx("token:" + token + ":userId", save.getId().toString(), 7, TimeUnit.DAYS);
        SysToken sysToken = new SysToken();
        sysToken.setToken(token);
        sysToken.setUserId(save.getId());
        sysToken.setUser(hideSensitiveInfo(save));
        return sysToken;
    }

    @Override
    public SysToken thirdLogin(Long userId) {
        SysUser byId = getById(userId);
        // 生成token字符串
        String token = IdUtil.simpleUUID();
        // 将token存入redis
        redisUtil.setEx("token:" + token + ":userId", userId.toString(), 7, TimeUnit.DAYS);
        SysToken sysToken = new SysToken();
        sysToken.setToken(token);
        sysToken.setUserId(userId);
        sysToken.setUser(byId);
        return sysToken;
    }

    @Override
    public SysUser updateAvatar(Long fileId) {
        Long idOfSelf = getIdOfSelf();

        return null;
    }

    @Override
    public SysUser getWithoutHideSensitiveInfo() {
        String redisKey = "token:" + requestHeaderUtil.getToken() + ":userId";
        String userId = redisUtil.get(redisKey);
        if (userId == null) {
            throw exceptionService.getException("loginStateExpired");
        }
        SysUser withoutHideSensitiveInfoById = getWithoutHideSensitiveInfoById(Long.parseLong(userId));
        if (withoutHideSensitiveInfoById != null) {
            return withoutHideSensitiveInfoById;
        }
        return repository.findById(getIdOfSelf()).orElseThrow(() -> exceptionService.getException("userNotExist"));
    }

    @Override
    public SysUser getWithoutHideSensitiveInfoById(Long id) {
        String redisKey = "userId:" + id + ":SysUser";
        String s = redisUtil.get(redisKey);
        if (s != null) {
            return JSON.parseObject(s, SysUser.class);
        }
        SysUser user = repository.findById(id).orElseThrow(() -> exceptionService.getException("userNotExist"));
        redisUtil.set(redisKey, JSON.toJSONString(user));
        return user;
    }

    @Override
    public void refreshToken() {
        String token = requestHeaderUtil.getToken();
        redisUtil.expire("token:" + token + ":userId", 7, TimeUnit.DAYS);
    }

    @Override
    public SysUser save(SysUser user) {
        SysUser save = repository.save(user);
        String redisKey = "userId:" + save.getId() + ":SysUser";
        redisUtil.setEx(redisKey, JSON.toJSONString(save), 7, TimeUnit.DAYS);
        return save;
    }
}
