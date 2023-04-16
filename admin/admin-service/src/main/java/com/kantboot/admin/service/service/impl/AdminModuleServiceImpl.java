package com.kantboot.admin.service.service.impl;

import com.kantboot.admin.module.entity.AdminMenu;
import com.kantboot.admin.service.repository.AdminModuleRepository;
import com.kantboot.admin.service.service.IAdminModuleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理员模块服务实现
 * 用于实现管理员模块的服务接口
 * @author 方某方
 */
@Service
public class AdminModuleServiceImpl implements IAdminModuleService {

    @Resource
    private AdminModuleRepository repository;

    @Override
    public List<AdminMenu> getMenus() {
        return repository.findByMenuCodeOfDirectoryIsNullOrderByPriorityDesc();
    }

    @Override
    public List<AdminMenu> getNoDirectoryMenus() {
        return repository.findByDirectoryIsFalseOrderByPriorityDesc();
    }
}
