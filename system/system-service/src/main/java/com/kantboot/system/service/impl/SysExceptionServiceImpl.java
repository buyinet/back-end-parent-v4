package com.kantboot.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.kantboot.system.module.entity.SysException;
import com.kantboot.system.repository.SysExceptionRepository;
import com.kantboot.system.service.ISysDictI18nService;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.util.common.exception.BaseException;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 系统异常的服务实现类
 * @author 方某方
 */
@Service
public class SysExceptionServiceImpl implements ISysExceptionService {

    @Resource
    private SysExceptionRepository repository;

    @Resource
    private ISysDictI18nService dictI18nService;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public SysException getByCode(String code) {
        // redis的key
        String key = "SysException:" + code;
        // 从redis中获取异常信息
        String value = redisUtil.get(key);
        // 如果异常信息不为空
        if (value != null) {
            // 返回异常信息
            return JSONObject.parseObject(value, SysException.class);
        }

        return repository.findByCode(code);
    }

    /**
     * 根据异常编码获取异常信息
     *
     * @param code 异常编码
     * @return 异常信息
     */
    @Override
    public BaseException getException(String code) {
        // 通过异常编码查询异常信息
        SysException byCode = getByCode(code);

        // 如果异常信息不为空
        if (byCode != null) {
            // 获取国际化异常信息
            String valueOfStateError = dictI18nService.getValue("stateError",code);

            // 如果国际化异常信息为空，使用默认异常信息
            if(valueOfStateError==null){
                // 默认异常信息
                valueOfStateError = byCode.getDescription();
            }

            // 返回异常信息
            return new BaseException(byCode.getState(), code , valueOfStateError);
        }

        // 如果异常信息为空，返回默认异常信息
        return new BaseException(3000,code, "请通过状态编码判断异常" );
    }
}
