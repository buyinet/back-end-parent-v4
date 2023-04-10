package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * 系统token的仓库
 * @author 方某方
 */
public interface SysTokenRepository extends JpaRepository<SysToken, Long>, Repository<SysToken, Long> {

    /**
     * 根据token获取token信息
     * @param token token
     * @return token信息
     */
    SysToken findByToken(String token);

    /**
     * 根据token获取数量
     * @param token token
     * @return 数量
     */
    int countByToken(String token);

    /**
     * 根据token删除token信息
     * @param token token
     */
    void deleteByToken(String token);


}
