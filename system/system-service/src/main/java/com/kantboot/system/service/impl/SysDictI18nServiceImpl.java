package com.kantboot.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kantboot.system.module.entity.SysDictI18n;
import com.kantboot.system.repository.SysDictI18nRepository;
import com.kantboot.system.repository.SysLanguageRepository;
import com.kantboot.system.service.ISysDictI18nService;
import com.kantboot.util.common.http.HttpRequestHeaderUtil;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     基于字典表的国际化服务接口
 * </p>
 * @author 方某方
 */
@Service
public class SysDictI18nServiceImpl implements ISysDictI18nService {

    @Resource
    private SysDictI18nRepository repository;

    @Resource
    private SysLanguageRepository languageRepository;

    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil;

    @Resource
    private RedisUtil redisUtil;


    @Override
    public Map<String, String> getMap(String languageCode, String dictGroupCode) {
        // redis的key
        String redisKey="dictI18n:" + languageCode + ":" + dictGroupCode;

        // 先从redis中获取
        String redisValue = redisUtil.get(redisKey);

        if (redisValue != null) {
            // 如果redis中有数据，则直接返回redis中的数据
            return JSON.parseObject(redisValue, HashMap.class);
        }

        // 如果redis中没有数据，则从数据库中查询
        Map<String, String> map = new HashMap<>(100);

        List<SysDictI18n> byLanguageCodeAndDictParentCode = repository.findByLanguageCodeAndDictGroupCode(languageCode,
                dictGroupCode);

        // 将查询到的数据转换为map
        for (SysDictI18n sysDictI18n : byLanguageCodeAndDictParentCode) {
            map.put(sysDictI18n.getDictCode(), sysDictI18n.getValue());
        }

        redisUtil.set(redisKey, JSON.toJSONString(map));

        return map;
    }

    @Override
    public Map<String, String> getMap(String dictGroupCode) {
        return getMap(httpRequestHeaderUtil.getLanguageCode(), dictGroupCode);
    }

    @Override
    public String getValue(String languageCode, String dictGroupCode, String dictCode) {
        // redis的key
        String redisKey="dictI18n:" + languageCode + ":" + dictGroupCode + ":" + dictCode;

        // 先从redis中获取
        String redisValue = redisUtil.get(redisKey);

        if (redisValue != null) {
            // 如果redis中有数据，则直接返回redis中的数据
            return redisValue;
        }

        // 如果redis中没有数据，则从数据库中查询
        String value = repository.findByLanguageCodeAndDictGroupCodeAndDictCode(languageCode, dictGroupCode, dictCode).getValue();
        redisUtil.set(redisKey, value);
        return value;
    }

    @Override
    public String getValue(String dictGroupCode, String dictCode) {
        return getValue(httpRequestHeaderUtil.getLanguageCode(), dictGroupCode, dictCode);
    }

}
