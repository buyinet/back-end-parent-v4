package com.kantboot.system.service;


import com.kantboot.util.common.result.RestResult;

/**
 * 状态成功的服务接口
 * @author 方某方
 */
public interface IStateSuccessService {

    /**
     * 返回成功的状态
     * @param data 数据
     * @param code 编码，用于获取国际化的值
     * @return 成功的状态
     */
    RestResult success(Object data, String code);

    /**
     * 返回成功的状态
     * @param data 数据
     * @param code 编码，用于获取国际化的值
     * @param operationCode 操作编码
     * @return 成功的状态
     */
    RestResult success(Object data,String code,String operationCode);
}
