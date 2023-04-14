package com.kantboot.third.party.wechat.controller;

import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.third.party.wechat.service.service.ITpWechatUserService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 第三方微信用户控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/thirdParty/wechatUser")
public class TpWechatUserController {

    @Resource
    private ITpWechatUserService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 微信小程序登录
     * @param code
     * @return
     */
    @RequestMapping("/loginInMp")
    public RestResult loginInMp(String code){
        return stateSuccessService.success(service.loginInMp(code), "loginSuccess");
    }


}
