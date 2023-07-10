package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysUserOnline;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户在线信息Repository接口
 * @author 方某方
 */
public interface SysUserOnlineRepository
extends JpaRepository<SysUserOnline, Long>
{

}
