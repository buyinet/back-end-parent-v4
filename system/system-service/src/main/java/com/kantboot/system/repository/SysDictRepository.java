package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysDict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * 字典的仓库接口
 * @author 方某方
 */
public interface SysDictRepository extends
        Repository<SysDict, Long>, JpaRepository<SysDict, Long>
{
}
