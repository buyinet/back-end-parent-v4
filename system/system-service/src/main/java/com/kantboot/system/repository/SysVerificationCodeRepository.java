package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * 验证码
 * 用于存储验证码
 * @author 方某方
 */
public interface SysVerificationCodeRepository
extends JpaRepository<SysVerificationCode, Long>
{

    /**
     * 根据发送目标、类型、动作查找验证码并且验证码未过期
     * @param sendTo 发送目标
     * @param typeCode 类型
     * @param actionCode 动作
     * @param gmtExpire 过期时间
     * @return 验证码列表
     */
    List<SysVerificationCode> findBySendToAndTypeCodeAndActionCodeAndGmtExpireAfter(
            String sendTo, String typeCode, String actionCode, Date gmtExpire
    );

}
