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

    @Override
    public List<SysPermission> getList() {
        List<SysPermission> all = repository.findAll();
        return all;
    }

    @Override
    public SysPermission getByUri(String uri) {
        SysPermission byUri = repository.findByUri(uri);
        return byUri;
    }
}
