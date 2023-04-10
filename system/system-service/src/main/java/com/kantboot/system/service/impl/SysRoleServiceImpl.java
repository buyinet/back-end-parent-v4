package com.kantboot.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kantboot.system.module.entity.SysRole;
import com.kantboot.system.service.ISysDictI18nService;
import com.kantboot.system.service.ISysRoleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 系统角色服务实现类
 * @author 方某方
 */
@Service
@Slf4j
public class SysRoleServiceImpl implements ISysRoleService {

    @Resource
    private ISysDictI18nService dictI18nService;

    @Override
    public Set<SysRole> getByRoles(Set<SysRole> roles) {
        // 记录开始时间
        long start = System.currentTimeMillis();

        // 获取角色国际化字典
        Map<String, String> roleMap = dictI18nService.getMap("role");

        // 有顺序的set集合，遍历时，会按照添加的顺序遍历
        Set<SysRole> roleSet = new LinkedHashSet<>(roles);
        // 按照优先级从大到小排序
        roleSet.stream().sorted((o1, o2) -> o2.getPriority() - o1.getPriority());

        for (SysRole role : roleSet) {
            String name = roleMap.get(role.getCode());
            if(name!=null){
                // 如果国际化角色名称不为空，则设置角色名称为国际化角色名称
                role.setName(name);
            }
        }


        // 记录结束时间
        long end = System.currentTimeMillis();
        log.info("获取角色国际化名称耗时：{}ms，{}", end - start,JSON.toJSONString(roleSet));
        return roleSet;
    }

    @Override
    public List<SysRole> getByRoles(List<SysRole> roles) {
        // 记录开始时间
        long start = System.currentTimeMillis();

        // 获取角色国际化字典
        Map<String, String> roleMap = dictI18nService.getMap("role");

        for (SysRole role : roles) {
            String name = roleMap.get(role.getCode());
            if(name!=null){
                // 如果国际化角色名称不为空，则设置角色名称为国际化角色名称
                role.setName(name);
            }
        }

        try{
            // 按照优先级从大到小排序
            roles.sort((o1, o2) -> o2.getPriority() - o1.getPriority());
        }catch (Exception e){
            log.error("排序异常，大体忽视",e.getMessage());
        }

        // 记录结束时间
        long end = System.currentTimeMillis();
        log.info("获取角色国际化名称耗时：{}ms，{}", end - start,JSON.toJSONString(roles));
        return roles;
    }
}
