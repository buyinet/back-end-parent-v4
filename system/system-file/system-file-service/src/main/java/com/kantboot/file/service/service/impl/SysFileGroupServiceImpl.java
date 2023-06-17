package com.kantboot.file.service.service.impl;

import com.kantboot.file.service.repository.SysFileGroupRepository;
import com.kantboot.file.service.service.ISysFileGroupService;
import com.kantboot.system.file.SysFileGroup;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 文件组管理的Service接口实现类
 * @author 方某方
 */
@Service
public class SysFileGroupServiceImpl implements ISysFileGroupService {

    @Resource
    SysFileGroupRepository repository;

    @Resource
    RedisUtil redisUtil;

    @Override
    public SysFileGroup getByCode(String code) {
        SysFileGroup byCode = repository.findByCode(code);
        return byCode;
    }

    @Override
    public String getPathByCode(String code) {
        String redisKey = "fileGroupId:" + code + ":path";
        String s = redisUtil.get(redisKey);
        if (s != null) {
            return s;
        }
        SysFileGroup byCode = repository.findByCode(code);
        if (byCode != null) {
            redisUtil.set(redisKey, byCode.getPath());
            return byCode.getPath();
        }
        return null;
    }
}
