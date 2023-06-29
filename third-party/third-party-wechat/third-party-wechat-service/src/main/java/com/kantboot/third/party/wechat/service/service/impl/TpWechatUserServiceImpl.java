package com.kantboot.third.party.wechat.service.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.kantboot.api.util.wechat.mp.login.Code2Session;
import com.kantboot.system.module.entity.SysToken;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.system.service.ISysSettingService;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.third.party.module.entity.TpWechatUser;
import com.kantboot.third.party.wechat.service.repositoty.TpWechatUserRepository;
import com.kantboot.third.party.wechat.service.service.ITpWechatUserService;
import com.kantboot.util.common.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * <p>
 *     微信用户表 服务实现类
 * </p>
 * @author 方某方
 */
@Service
public class TpWechatUserServiceImpl implements ITpWechatUserService {


    @Resource
    private TpWechatUserRepository repository;

    @Resource
    private ISysSettingService settingService;


    @Resource
    ISysExceptionService exceptionService;

    @Resource
    private ISysUserService userService;

    @Override
    public String getOpenIdByCode(String code) {
        // 获取微信小程序appId
        String appid = settingService.getValue("wechat", "mpAppId");
        // 获取微信小程序appSecret
        String appSecret = settingService.getValue("wechat", "mpAppSecret");

        Code2Session.Param param = new Code2Session.Param();
        param.setAppid(appid);
        param.setSecret(appSecret);
        param.setJsCode(code);
        param.setGrantType("authorization_code");
        Code2Session.Result result;
        try{
            result = Code2Session.getResult(param);
        }catch (BaseException e){
            throw exceptionService.getException(e.getStateCode());
        }
        return result.getOpenid();
    }

    @Override
    @Transactional
    public SysToken loginInMp(String code) {
        // 获取微信小程序appId
        String appid = settingService.getValue("wechat", "mpAppId");
        // 获取微信小程序appSecret
        String appSecret = settingService.getValue("wechat", "mpAppSecret");

        Code2Session.Param param = new Code2Session.Param();
        param.setAppid(appid);
        param.setSecret(appSecret);
        param.setJsCode(code);
        param.setGrantType("authorization_code");
        Code2Session.Result result;
        try{
            result = Code2Session.getResult(param);
        }catch (BaseException e){
            throw exceptionService.getException(e.getStateCode());
        }

        // 根据unionid查询用户
        TpWechatUser byUnionid = repository.findByUnionid(result.getUnionid());
        if (byUnionid != null) {
            return userService.thirdLogin(byUnionid.getUserId());
        }

        SysUser sysUser = new SysUser();

        // 注册用户
        SysToken register = userService.thirdRegister(sysUser);

        // 保存微信用户
        TpWechatUser tpWechatUser = new TpWechatUser();
        tpWechatUser.setUserId(register.getUserId());
        tpWechatUser.setOpenid(result.getOpenid());
        tpWechatUser.setUnionid(result.getUnionid());
        repository.save(tpWechatUser);

        return register;
    }

    @Override
    public TpWechatUser getSelf() {
        Long idOfSelf = userService.getIdOfSelf();
        return repository.findByUserId(idOfSelf);
    }
}



















