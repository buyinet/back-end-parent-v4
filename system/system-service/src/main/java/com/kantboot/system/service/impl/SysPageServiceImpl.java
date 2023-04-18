package com.kantboot.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kantboot.system.module.entity.RelSysPageAndSysDict;
import com.kantboot.system.module.entity.SysDict;
import com.kantboot.system.module.entity.SysPage;
import com.kantboot.system.repository.RelSysPageAndSysDictRepository;
import com.kantboot.system.repository.SysPageRepository;
import com.kantboot.system.service.ISysDictI18nService;
import com.kantboot.system.service.ISysPageService;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 页面服务实现类
 *
 * @author 方某方
 */
@Service
public class SysPageServiceImpl implements ISysPageService {

    @Resource
    private SysPageRepository repository;
    @Resource
    private ISysDictI18nService dictI18nService;

    @Resource
    private RelSysPageAndSysDictRepository relSysPageAndSysDictRepository;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public SysPage get(String code) {
        Map<String, String> pageValue = dictI18nService.getMap("pageCode");
        SysPage byCode = repository.findByCode(code);
        List<RelSysPageAndSysDict> byPageCode = relSysPageAndSysDictRepository.findByPageCode(code);
        if (byPageCode.size() == 0) {
            byCode.setDictList(new ArrayList<>());
            return byCode;
        }
        List<SysDict> dictList =new ArrayList<>();
        byPageCode.forEach(relSysPageAndSysDict -> {
            String dictCode = relSysPageAndSysDict.getDictCode();
            String dictValue = pageValue.get(dictCode);
            dictList.add(new SysDict().setCode(dictCode).setValue(dictValue));
        });
        byCode.setDictList(dictList);
        return byCode;
    }

    @Override
    public List<SysDict> getDictList(String code) {
        return get(code).getDictList();
    }

    @Override
    public Map<String, String> getDictMap(String code) {
        // redis的key
        String key = "dictI18n:pageCode:" + code;
        // 从redis中获取
        String value = redisUtil.get(key);
        if (value != null) {
            return JSON.parseObject(value, Map.class);
        }

        Map<String, String> dictMap = new HashMap<>();
        List<SysDict> dictList = getDictList(code);
        dictList.forEach(dict -> {
            dictMap.put(dict.getCode(), dict.getValue());
        });

        return dictMap;
    }
}










