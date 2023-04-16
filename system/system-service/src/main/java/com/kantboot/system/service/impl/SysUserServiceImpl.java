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

import java.util.List;

/**
 * 用户服务实现类
 *
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

    @Resource
    private ISysRoleService roleService;

    /**
     * 对用户的手机号进行隐私保护，将中间4位数字用星号替代
     *
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
     *
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
     *
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
     *
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

    /**
     * 处理用户给客户端返回的信息
     *
     * @param user 用户
     *             1、隐藏敏感信息
     *             2、设置用户的角色信息
     * @return 处理后的用户
     */
    private SysUser handleUser(SysUser user) {
        // 1、隐藏敏感信息
        SysUser result = hideSensitiveInfo(user);
        // 2、设置用户的角色信息
        result.setRoles(roleService.getByRoles(user.getRoles()));
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

        // 保存用户
        SysUser save = repository.save(user.setRoles(List.of(new SysRole().setCode(defaultRole))));
        // 创建token
        SysToken token = tokenService.createToken(save.getId());

        // 隐藏敏感信息
        SysUser handleUser = handleUser(token.getUser());
        token.setUser(handleUser);

        // 设置token的用户信息
        log.info("用户注册成功，用户信息为：{}，token信息为：{}", handleUser, token);

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

        // 创建token
        SysToken token = tokenService.createToken(user.getId());
        token.setUser(handleUser(token.getUser()));
        return token;
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
        SysUser result = repository.findById(id).orElseThrow(() -> exceptionService.getException("userNotExist"));
        return handleUser(result);
    }

    @Override
    public SysUser getSelf() {
        SysToken self = tokenService.getSelf();
        return handleUser(self.getUser());
    }

    @Override
    public Long getIdOfSelf() {
        return getSelf().getId();
    }

    @Override
    public SysToken thirdRegister(SysUser user) {
        // 获取用户注册时的默认角色
        String defaultRole = settingService.getValue("user", "newUserRegisterRoleCode");
        // 第三方注册的用户，用户名为空
        user.setUsername(null);
        // 第三方注册的用户，密码为空
        user.setPassword(null);
        // 第三方注册的用户，手机号为空
        user.setPhone(null);
        // 第三方注册的用户，邮箱为空
        user.setEmail(null);
        // 第三方注册的用户，头像为空
        user.setFileIdOfAvatar(null);

        // 设置用户的角色
        user.setRoles(List.of(new SysRole().setCode(defaultRole)));

        // 保存用户
        tokenService.createToken(user.getId());
        SysToken token = tokenService.createToken(user.getId());
        token.setUser(handleUser(token.getUser()));
        return token;
    }

    @Override
    public SysToken thirdLogin(Long userId) {
        // 创建token
        SysToken token = tokenService.createToken(userId);
        // 处理用户信息
        token.setUser(handleUser(token.getUser()));
        return token;
    }
}
