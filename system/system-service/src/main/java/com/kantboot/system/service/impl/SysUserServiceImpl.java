package com.kantboot.system.service.impl;

import com.kantboot.system.module.dto.SecurityLoginAndRegisterDTO;
import com.kantboot.system.module.entity.SysToken;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.repository.SysUserRepository;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.system.service.ISysTokenService;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.util.common.password.KantbootPassword;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 * @author 方某方
 */
@Service
@Log4j2
public class SysUserServiceImpl implements ISysUserService {

    @Resource
    private SysUserRepository repository;

    @Resource
    private ISysExceptionService exceptionService;

    @Resource
    private KantbootPassword kantbootPassword;

    @Resource
    private ISysTokenService tokenService;

    @Override
    public SysUser hideSensitiveInfo(SysUser user) {

        String phone = user.getPhone();
        // 如果手机号不为空
        if (phone != null) {
            // 获取手机号长度
            int length = phone.length();
            // 只保留后四位，其它换成*
            user.setPhone(phone.substring(0,length - 4).replaceAll(".", "*")+phone.substring(length - 4));
        }

        String email = user.getEmail();
        // 如果邮箱不为空
        if (email != null) {
            // 获取邮箱长度
            int length = email.length();
            // 获取@前面的字符串
            String substring = email.substring(0, email.indexOf("@"));
            // 获取@后面的字符串
            String substring1 = email.substring(email.indexOf("@"));
            // 只保留@前面的字符串的最后四位，其它换成*
            user.setEmail(substring.substring(0,substring.length() - 4).replaceAll(".", "*")+substring.substring(substring.length() - 4)+substring1);
        }

        // 清空密码
        user.setPassword(null);
        // 清空创建时间
        user.setGmtCreate(null);
        // 清空修改时间
        user.setGmtModified(null);

        return user;
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
        // 保存用户
        SysUser save = repository.save(user);
        // 创建token
        SysToken token = tokenService.createToken(save.getId());

        // 隐藏敏感信息
        SysUser userOfHide = hideSensitiveInfo(token.getUser());
        token.setUser(userOfHide);

        // 设置token的用户信息
        log.info("用户注册成功，用户信息为：{}，token信息为：{}",userOfHide,token);

        return token;
    }

    @Override
    public SysToken securityRegister(SecurityLoginAndRegisterDTO dto) {
        return null;
    }


}
