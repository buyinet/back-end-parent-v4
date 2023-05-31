package com.kantboot.system.service;

import com.kantboot.system.module.entity.SysFileGroup;

/**
 * 文件组的服务接口
 * @author 方某方
 */
public interface ISysFileGroupService {


    /**
     * 新建文件组
     * @param code 文件组编码
     *             1. 不能重复
     *             2. 不能为null
     *             3. 不能为""
     * @param name 文件组名称
     * @return 文件组
     */
    SysFileGroup create(String code, String name);



}
