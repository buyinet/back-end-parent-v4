package com.kantboot.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kantboot.system.module.entity.SysPermission;
import com.kantboot.system.repository.SysPermissionRepository;
import com.kantboot.system.service.ISysPermissionService;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限服务实现类
 * @author 方某方
 */
@Service
public class SysPermissionServiceImpl implements ISysPermissionService {

    @Resource
    private SysPermissionRepository repository;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<SysPermission> getList() {
        List<SysPermission> all = repository.findAll();
        return all;
    }

    @Override
    public SysPermission getByUri(String uri) {
        // redis中的key
        String key = "sys:permission:uri:" + uri;
        // 从redis中获取
        SysPermission permission = JSON.parseObject(redisUtil.get(key), SysPermission.class);
        if (permission != null) {
            return permission;
        }
        // 如果redis中没有，则从数据库中查询
        SysPermission byUri = repository.findByUri(uri);
        // 将查询到的数据放入redis中
        redisUtil.set(key, JSON.toJSONString(byUri));
        return byUri;
    }
}
