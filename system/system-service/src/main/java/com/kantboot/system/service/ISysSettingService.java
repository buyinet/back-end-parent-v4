package com.kantboot.system.service;

import java.util.Map;

/**
 * 系统设置服务接口
 * @author 方某方
 */
public interface ISysSettingService {

    /**
     * 获取系统设置的map
     * @param groupCode 分组编码
     * @return map
     */
    Map<String, String> getMap(String groupCode);

    /**
     * 获取系统设置
     * @param groupCode 分组编码
     * @param code 设置的编码
     * @return 设置的值
     */
    String getValue(String groupCode, String code);


}
