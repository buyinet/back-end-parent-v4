package com.kantboot.system.controller;

import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.system.service.ISysVerificationCodeService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/verificationCode")
public class SysVerificationCodeController {

    @Resource
    private ISysVerificationCodeService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    @RequestMapping("/send")
    public RestResult send(
            String sendTo,
            String typeCode,
            String actionCode) {
        // 调用service层的发送验证码方法，返回结果，并返回成功信息
        service.sendVerificationCode(sendTo, typeCode, actionCode);
        return stateSuccessService.success(null,"sendSuccess");
    }

}
