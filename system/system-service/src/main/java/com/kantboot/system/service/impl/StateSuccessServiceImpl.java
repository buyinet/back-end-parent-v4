package com.kantboot.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.system.service.ISysDictI18nService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 状态成功的服务实现类
 * @author 方某方
 */
@Service
public class StateSuccessServiceImpl implements IStateSuccessService {

    @Resource
    private ISysDictI18nService dictI18nService;

    @Override
    public RestResult success(Object data, String code) {
        return RestResult.success(data, dictI18nService.getValue("stateSuccess", code));
    }

    @Override
    public RestResult success(Object data, String code, String operationCode) {
        return success(data, code).setOperationCode(operationCode);
    }

}
