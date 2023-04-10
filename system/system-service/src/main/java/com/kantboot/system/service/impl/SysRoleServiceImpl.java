package com.kantboot.system.service.impl;

import com.kantboot.system.module.entity.SysRole;
import com.kantboot.system.service.ISysDictI18nService;
import com.kantboot.system.service.ISysRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * 系统角色服务实现类
 * @author 方某方
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {

    @Resource
    private ISysDictI18nService dictI18nService;

    @Override
    public Set<SysRole> getByRoles(Set<SysRole> roles) {
        Map<String, String> roleMap = dictI18nService.getMap("role");
        for (SysRole role : roles) {
            if(roleMap.get(role.getCode())!=null){
                // 如果国际化角色名称不为空，则设置角色名称为国际化角色名称
                role.setName(roleMap.get(role.getCode()));
            }
        }
        return roles;
    }

}
