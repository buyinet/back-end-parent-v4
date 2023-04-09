package com.kantboot.system.service;

import com.kantboot.system.module.entity.SysException;
import com.kantboot.util.common.exception.BaseException;

/**
 * 系统异常的服务接口
 * @author 方某方
 */
public interface ISysExceptionService {

    /**
     * 根据异常编码获取系统异常
     * @param code 异常编码
     * @return 异常信息
     */
    SysException getByCode(String code);

    /**
     * 根据异常编码获取异常信息
     * @param code 异常编码
     * @return 异常信息
     */
    BaseException getException(String code);

}
