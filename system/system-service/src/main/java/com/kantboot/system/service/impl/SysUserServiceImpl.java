package com.kantboot.system.service.impl;

import com.kantboot.system.module.dto.SecurityLoginAndRegisterDTO;
import com.kantboot.system.module.entity.SysRole;
import com.kantboot.system.module.entity.SysToken;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.repository.SysUserRepository;
import com.kantboot.system.service.*;
import com.kantboot.util.common.password.KantbootPassword;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Set;

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

    @Resource
    private IRsaService rsaService;

    @Resource
    private ISysSettingService settingService;

    /**
     * 对用户的手机号进行隐私保护，将中间4位数字用星号替代
     * @param phone 用户手机号
     * @return 隐私保护后的手机号
     */
    private String protectPhoneNumber(String phone) {
        int length = phone.length();
        // 将手机号中间四位替换为星号
        return String.format("%s%s", phone.substring(0, length - 4).replace(".", "\\*"), phone.substring(length - 4));
    }

    /**
     * 对用户的邮箱进行隐私保护，只保留@前面的字符串的最后四位，其它换成*
     * @param email 用户邮箱
     * @return 隐私保护后的邮箱
     */
    public String protectEmail(String email) {
        // 获取@前面的字符串
        String prefix = getEmailPrefix(email);
        // 获取@后面的字符串
        String suffix = getEmailSuffix(email);
        // 只保留@前面的字符串的最后四位，其它换成*
        return String.format("%s%s%s", prefix.substring(0, prefix.length() - 4).replace(".", "\\*"), prefix.substring(prefix.length() - 4), suffix);
    }

    /**
     * 获取邮箱的@前面的字符串
     * @param email 用户邮箱
     * @return 邮箱的@前面的字符串
     */
    private String getEmailPrefix(String email) {
        int index = email.indexOf('@');
        if (index < 0) {
            return email;
        }
        return email.substring(0, index);
    }

    /**
     * 获取邮箱的@后面的字符串
     * @param email 用户邮箱
     * @return 邮箱的@后面的字符串
     */
    private String getEmailSuffix(String email) {
        int index = email.indexOf('@');
        if (index < 0) {
            return "";
        }
        return email.substring(index);
    }



    @Override
    public SysUser hideSensitiveInfo(SysUser user) {

        String phone = user.getPhone();
        // 如果手机号不为空
        if (phone != null) {
            // 隐私保护手机号
            user.setPhone(protectPhoneNumber(phone));
        }

        String email = user.getEmail();
        // 如果邮箱不为空
        if (email != null) {
            // 隐私保护邮箱
            user.setEmail(protectEmail(email));
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
        // 获取用户注册时的默认角色
        String defaultRole = settingService.getValue("newUserRegisterRoleCode", "user");
        Set<SysRole> roles = Set.of(new SysRole().setCode(defaultRole));

        // 保存用户
        SysUser save = repository.save(user.setRoles(roles));
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
        // 获取加密用户名的公钥
        String publicKeyOfUsername = dto.getPublicKeyOfUsername();
        // 获取加密密码的公钥
        String publicKeyOfPassword = dto.getPublicKeyOfPassword();

        // 解密用户名
        String username = rsaService.decrypt(dto.getUsername(), publicKeyOfUsername);
        // 解密密码
        String password = rsaService.decrypt(dto.getPassword(), publicKeyOfPassword);

        return register(username, password);
    }


}
