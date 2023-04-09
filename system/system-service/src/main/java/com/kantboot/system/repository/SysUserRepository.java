package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * 用户仓库
 * @author 方某方
 */
public interface SysUserRepository extends JpaRepository<SysUser, Long>, Repository<SysUser, Long> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    SysUser findByUsername(String username);
}
