package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysRole;
import jakarta.annotation.Resource;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * 系统角色数据访问接口
 * @author 方某方
 */
public interface SysRoleRepository
extends JpaRepository<SysRole, Long> {

    SysRole findByCode(String code);

}
