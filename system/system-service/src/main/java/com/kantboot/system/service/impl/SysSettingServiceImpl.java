package com.kantboot.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kantboot.system.module.entity.SysSetting;
import com.kantboot.system.repository.SysSettingRepository;
import com.kantboot.system.service.ISysSettingService;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统设置服务实现类
 *
 * @author 方某方
 */
@Service
public class SysSettingServiceImpl implements ISysSettingService {


    @Resource
    private SysSettingRepository repository;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public Map<String, String> getMap(String groupCode) {

        // redis的key
        String key = "sysSetting:" + groupCode;

        // 从redis中获取
        String value = redisUtil.get(key);

        if (value != null) {
            // 如果redis中有数据，则直接返回redis中的数据
            return JSON.parseObject(value, HashMap.class);
        }

        HashMap<String, String> map = new HashMap<>(100);
        List<SysSetting> byGroupCode = repository.findByGroupCode(groupCode);
        for (SysSetting sysSetting : byGroupCode) {
            map.put(sysSetting.getCode(), sysSetting.getValue());
        }

        // 将查询到的数据放入redis中
        redisUtil.set(key, JSON.toJSONString(map));

        return map;
    }

    @Override
    public String getValue(String groupCode, String code) {

        // redis的key
        String key = "sysSetting:" + groupCode + ":" + code;

        // 从redis中获取
        String value = redisUtil.get(key);

        if (value != null) {
            // 如果redis中有数据，则直接返回redis中的数据
            return value;
        }

        // 如果redis中没有数据，则从数据库中查询
        value = repository.findByGroupCodeAndCode(groupCode, code).getValue();

        // 将查询到的数据放入redis中
        redisUtil.set(key, value);

        return value;
    }

}
