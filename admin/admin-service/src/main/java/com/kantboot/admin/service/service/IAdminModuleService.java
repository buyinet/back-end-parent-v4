package com.kantboot.admin.service.service;

import com.kantboot.admin.module.entity.AdminMenu;

import java.util.List;

/**
 * 管理员模块服务
 * 用于定义管理员模块的服务接口
 * @author 方某方
 */
public interface IAdminModuleService {

    /**
     * 获取所有菜单
     * @return 所有菜单
     */
    List<AdminMenu> getMenus();

    /**
     * 获取所有菜单
     * @return 菜单
     */
    List<AdminMenu> getNoDirectoryMenus();
}
