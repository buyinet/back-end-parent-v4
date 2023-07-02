package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 系统token仓库接口
 * @author 方某方
 */
public interface SysTokenRepository
extends JpaRepository<SysToken,Long>
{

    /**
     * 根据token获取token信息
     * @param token token
     * @return token信息
     */
    SysToken findByToken(String token);
}
