package com.kantboot.file.service.service;

import com.kantboot.system.file.SysFileGroup;

/**
 * 文件组管理的Service接口
 * @author 方某某
 */
public interface ISysFileGroupService {


    /**
     * 根据文件组编码获取文件组
     * @param code 文件组编码
     * @return 文件组
     */
    SysFileGroup getByCode(String code);

    /**
     * 根据文件组编码获取文件组路径
     * @param code 文件组编码
     * @return 文件组路径
     */
    String getPathByCode(String code);

}
