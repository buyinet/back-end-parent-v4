package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * 系统token的仓库
 * @author 方某方
 */
public interface SysTokenRepository extends JpaRepository<SysToken, Long>, Repository<SysToken, Long> {

}
