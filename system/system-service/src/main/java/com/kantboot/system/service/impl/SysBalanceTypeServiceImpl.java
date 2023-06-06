package com.kantboot.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kantboot.system.module.entity.SysBalanceType;
import com.kantboot.system.repository.SysBalanceTypeRepository;
import com.kantboot.system.service.ISysBalanceTypeService;
import com.kantboot.util.common.http.HttpRequestHeaderUtil;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 余额类型服务实现类
 * @author 方某方
 */
@Service
public class SysBalanceTypeServiceImpl implements ISysBalanceTypeService {

    @Resource
    private SysBalanceTypeRepository repository;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    HttpRequestHeaderUtil httpRequestHeaderUtil;

    @Override
    public Map<String, String> getMap() {
        String redisKey = "balanceTypeMap:"+httpRequestHeaderUtil.getLanguageCode();

        if(redisUtil.hasKey(redisKey)){
            return JSON.parseObject(redisUtil.get(redisKey), Map.class);
        }

        Map<String, String> result = new HashMap<>(10);
        List<SysBalanceType> all = repository.findAll();
        for (SysBalanceType sysBalanceType : all) {
            result.put(sysBalanceType.getCode(), sysBalanceType.getName());
        }

        redisUtil.set(redisKey, JSON.toJSONString(result));
        return result;
    }
}
