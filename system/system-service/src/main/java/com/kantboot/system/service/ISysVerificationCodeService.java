package com.kantboot.system.service;

/**
 * 验证码的服务
 * @author 方某方
 */
public interface ISysVerificationCodeService {

    /**
     * 生成验证码
     * @return 验证码
     */
    String generateVerificationCode();

    /**
     * 发送验证码
     * @param sendTo 发送目标
     * @param typeCode 类型
     * @param actionCode 动作
     * @return 验证码
     */
    String sendVerificationCode(
            String sendTo,
            String typeCode,
            String actionCode);



}
