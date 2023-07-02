package com.kantboot.system.service;

import com.kantboot.system.module.entity.SysRole;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统角色服务接口
 * @author 方某方
 */
public interface ISysRoleService {

    /**
     * 根据角色进一步获取角色信息
     * @param roles 角色
     *              会进一步修改传入的对象
     *              例如：会采用国际化，会将角色名称转换为对应的语言
     * @return 角色信息
     */
    Set<SysRole> getByRoles(Set<SysRole> roles);


    /**
     * 根据角色进一步获取角色信息
     * @param roles 角色
     *              会进一步修改传入的对象
     *              例如：会采用国际化，会将角色名称转换为对应的语言
     * @return 角色信息
     */
    List<SysRole> getByRoles(List<SysRole> roles);

    /**
     * 根据角色code获取角色信息
     * @param code 角色code
     * @return 角色信息
     */
    SysRole getByCode(String code);

    /**
     * 获取角色信息国际化字典
     * @return 结果
     */
    Map<String,String> getI18nMap();

}
