package com.kantboot.system.service;

import com.kantboot.system.module.entity.SysPermission;

import java.util.List;

/**
 * 权限服务
 * @author 方某方
 */
public interface ISysPermissionService {

    /**
     * 获取所有权限
     * @return 权限列表
     */
    List<SysPermission> getList();


    /**
     * 根据uri获取权限
     * @param uri uri
     * @return 权限
     */
    SysPermission getByUri(String uri);


}
