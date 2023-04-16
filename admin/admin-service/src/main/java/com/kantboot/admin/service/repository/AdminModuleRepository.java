package com.kantboot.admin.service.repository;

import com.kantboot.admin.module.entity.AdminMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * 管理员模块仓库
 * @author 方某方
 */
public interface AdminModuleRepository extends Repository<AdminMenu,Long>, JpaRepository<AdminMenu,Long> {

    /**
     * 获取菜单
     * @return 菜单
     */
    List<AdminMenu> findByMenuCodeOfDirectoryIsNullOrderByPriorityDesc();

    /**
     * 获取不包括目录的菜单
     * @return 菜单
     */
    List<AdminMenu> findByDirectoryIsFalseOrderByPriorityDesc();
}
