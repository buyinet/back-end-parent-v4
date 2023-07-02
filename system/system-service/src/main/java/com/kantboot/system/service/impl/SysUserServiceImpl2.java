package com.kantboot.system.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.kantboot.system.module.dto.SecurityLoginAndRegisterDTO;
import com.kantboot.system.module.entity.SysException;
import com.kantboot.system.module.entity.SysRole;
import com.kantboot.system.module.entity.SysToken;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.repository.SysTokenRepository;
import com.kantboot.system.repository.SysUserRepository;
import com.kantboot.system.service.*;
import com.kantboot.util.common.exception.BaseException;
import com.kantboot.util.common.http.HttpRequestHeaderUtil;
import com.kantboot.util.common.password.KantbootPassword;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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

    @Resource
    private SysTokenRepository tokenRepository;

    @Resource
    private KantbootPassword kantbootPassword;

    @Resource
    private ISysSettingService settingService;

    @Resource
    private ISysRoleService roleService;

    @Resource
    private IRsaService rsaService;

    /**
     * 保存token
     * @param token token
     * @param userId 用户id
     * @return token
     */
    private SysToken saveToken(String token, Long userId) {
        String redisKey = "token:" + token + ":userId";

        SysToken sysToken = new SysToken();
        sysToken.setToken(token);
        sysToken.setUserId(userId);
        // 过期时间, 7天后过期
        sysToken.setGmtExpire(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000));

        // 保存token
        SysToken save = tokenRepository.save(sysToken);
        // 保存token到redis, 7天后过期
        redisUtil.setEx(redisKey, userId+"", 7, TimeUnit.DAYS);
        // 获取用户
        SysUser user = getById(userId);
        save.setUser(hideSensitiveInfo(user));

        return save;
    }


    /**
     * 保存token
     * @param userId 用户id
     * @return token
     */
    private SysToken saveToken(Long userId) {
        String token = IdUtil.simpleUUID();
        return saveToken(token, userId);
    }


    /**
     * 通过token获取用户
     * @param token token
     * @return 用户
     */
    private SysUser getUserByToken(String token) {
        String redisKey = "token:" + token + ":userId";
        String userId = redisUtil.get(redisKey);

        if (userId == null) {
            SysToken byToken = tokenRepository.findByToken(token);
            if (byToken == null) {
                // 提示用户未登录
                throw exceptionService.getException("notLogin");
            }
            userId = byToken.getUserId() + "";

            // 保存token到redis, 7天后过期
            redisUtil.setEx("token:" + token + ":userId", userId+"", 7, TimeUnit.DAYS);
        }


        try {
            return getById(Long.parseLong(userId));
        } catch (BaseException e) {
            // 如果用户不存在，提示用户未登录
            if (e.getStateCode().equals("userNotExist")) {
                // 提示用户未登录
                throw exceptionService.getException("notLogin");
            }
            throw e;
        }

    }


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
        // 根据用户名查询用户
        SysUser byUsername = repository.findByUsername(username);
        // 判断用户是否重复
        if (byUsername != null) {
            // 如果用户重复，抛出异常，提示客户端用户名重复
            throw exceptionService.getException("usernameRepeat");
        }
        // 密码加密
        String encodePassword = kantbootPassword.encode(password);
        // 创建用户
        SysUser user = new SysUser().setPassword(encodePassword).setUsername(username);
        // 获取用户注册时的默认角色
        String defaultRole = settingService.getValue("user", "newUserRegisterRoleCode");
        // 设置用户角色
        List<SysRole> roles = new ArrayList<>();
        roles.add(roleService.getByCode(defaultRole));
        user.setRoles(roles);
        // 保存用户
        SysUser save = repository.save(user);

        return saveToken(save.getId());
    }

    @Override
    public SysToken securityRegister(SecurityLoginAndRegisterDTO dto) {
        // 先用微信小程序，后面再加
        return null;
    }

    @Override
    public SysToken login(String account, String password) {
                // 根据用户名查询用户
        SysUser user = repository.findByUsername(account);
        if (user == null) {
            // 如果用户不存在，则继续根据手机号查询用户
            user = repository.findByPhone(account);
        }

        if (user == null) {
            // 如果用户不存在，则继续根据邮箱查询用户
            user = repository.findByEmailIgnoreCase(account);
        }

        if (user == null) {
            //如果用户不存在，抛出异常，提示客户端账号不存在
            throw exceptionService.getException("accountNotExist");
        }

        // 判断密码是否正确
        if (!kantbootPassword.matches(password, user.getPassword())) {
            // 如果密码不正确，抛出异常，提示客户端账号或密码错误
            throw exceptionService.getException("accountOrPasswordError");
        }

        return saveToken(user.getId());
    }

    @Override
    public SysToken securityLogin(SecurityLoginAndRegisterDTO dto) {
        // 获取加密账号的公钥
        String publicKeyOfAccount = dto.getPublicKeyOfAccount();
        // 获取加密密码的公钥
        String publicKeyOfPassword = dto.getPublicKeyOfPassword();

        // 解密账号
        String account = rsaService.decrypt(dto.getAccount(), publicKeyOfAccount);
        // 解密密码
        String password = rsaService.decrypt(dto.getPassword(), publicKeyOfPassword);

        return login(account, password);
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
        SysUser result = repository.findById(id).orElse(null);
        if (result == null) {
            throw exceptionService.getException("userNotExist");
        }
        // 将用户信息存入redis
        redisUtil.set(redisKey, JSON.toJSONString(result));
        return hideSensitiveInfo(result);
    }

    @Override
    public SysUser getSelf() {
        SysUser userByToken = getUserByToken(requestHeaderUtil.getToken());
        return userByToken;
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
        SysToken sysToken = saveToken(save.getId());
         return sysToken;
    }

    @Override
    public SysToken thirdLogin(Long userId) {
        SysToken sysToken = saveToken(userId);
        return sysToken;
    }

    @Override
    public SysUser updateAvatar(Long fileId) {
        Long idOfSelf = getIdOfSelf();

        return null;
    }

    @Override
    public SysUser getWithoutHideSensitiveInfo() {
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
        SysToken byToken = tokenRepository.findByToken(token);
        byToken.setGmtExpire(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000));
        tokenRepository.save(byToken);
    }

    @Override
    public SysUser save(SysUser user) {
        SysUser save = repository.save(user);
        String redisKey = "userId:" + save.getId() + ":SysUser";
        redisUtil.setEx(redisKey, JSON.toJSONString(save), 7, TimeUnit.DAYS);
        return save;
    }
}
