package com.kantboot.third.party.wechat.service.service;

import com.kantboot.system.module.entity.SysToken;
import com.kantboot.third.party.module.entity.TpWechatUser;

/**
 * 第三方微信用户服务
 * @author 方某方
 */
public interface ITpWechatUserService {

    /**
     * 通过code获取openid
     * @param code
     * @return openid
     */
    String getOpenIdByCode(String code);

    /**
     * 微信小程序登录
     * @param code
     * @return
     */
    SysToken loginInMp(String code);

    /**
     * 获取自己的微信用户信息
     * @return 微信用户信息
     */
    TpWechatUser getSelf();

}
