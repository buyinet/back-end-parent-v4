package com.kantboot.admin.service.service.impl;

import com.kantboot.admin.module.entity.AdminMenu;
import com.kantboot.admin.service.repository.AdminModuleRepository;
import com.kantboot.admin.service.service.IAdminModuleService;
import com.kantboot.system.service.ISysDictI18nService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 管理员模块服务实现
 * 用于实现管理员模块的服务接口
 * @author 方某方
 */
@Service
public class AdminModuleServiceImpl implements IAdminModuleService {

    @Resource
    private AdminModuleRepository repository;

    @Resource
    private ISysDictI18nService i18nService;


    @Override
    public List<AdminMenu> getMenus() {
        Map<String, String> adminMenuMap = i18nService.getMap("adminMenu");
        List<AdminMenu> list = repository.findByMenuCodeOfDirectoryIsNullOrderByPriorityDesc();
        list.forEach(menu -> {
            // 设置菜单名称
            String name = adminMenuMap.get(menu.getCode());
            menu.setName(name!=null?name:menu.getName());

            List<AdminMenu> children = menu.getChildren();

            // 设置子菜单名称
            children.forEach(child ->{
                String childName = adminMenuMap.get(child.getCode());
                child.setName(childName!=null?childName:child.getName());
            });

            // 降序排列
            children.sort((o1, o2) -> o2.getPriority() - o1.getPriority());
        });

        return list;
    }

    @Override
    public List<AdminMenu> getNoDirectoryMenus() {
        List<AdminMenu> byDirectoryIsFalseOrderByPriorityDesc = repository.findByDirectoryIsFalseOrderByPriorityDesc();
        Map<String, String> adminMenuMap = i18nService.getMap("adminMenu");
        byDirectoryIsFalseOrderByPriorityDesc.forEach(menu -> {
            String name = adminMenuMap.get(menu.getCode());
            menu.setName(name!=null?name:menu.getName());
        });
        return byDirectoryIsFalseOrderByPriorityDesc;
    }
}
