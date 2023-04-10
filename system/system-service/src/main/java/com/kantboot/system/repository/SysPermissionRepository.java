package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * 权限仓库
 * @author 方某方
 */
public interface SysPermissionRepository extends Repository<SysPermission,Long>, JpaRepository<SysPermission,Long> {

    /**
     * 根据code获取权限
     * @param code 权限编码
     * @return 权限
     */
    SysPermission findByCode(String code);

    /**
     * 根据uri获取权限，正则匹配
     * @param uri uri
     * @return 权限
     */
    SysPermission findByUri(String uri);

}
