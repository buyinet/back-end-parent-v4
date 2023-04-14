package com.kantboot.third.party.wechat.service.service;

import com.kantboot.system.module.entity.SysToken;

/**
 * 第三方微信用户服务
 * @author 方某方
 */
public interface ITpWechatUserService {

    /**
     * 微信小程序登录
     * @param code
     * @return
     */
    SysToken loginInMp(String code);

}
