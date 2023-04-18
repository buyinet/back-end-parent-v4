package com.kantboot.system.service;

import com.kantboot.system.module.entity.SysDict;
import com.kantboot.system.module.entity.SysPage;

import java.util.List;
import java.util.Map;

/**
 * 页面服务接口
 * @author 方某方
 */
public interface ISysPageService {

    /**
     * 根据页面编码查询页面
     * @param code 页面编码
     *             不能为空
     * @return 页面
     */
    SysPage get(String code);

    /**
     * 根据字典编码查询字典
     * @param code 字典编码
     *             不能为空
     * @return 字典
     */
    List<SysDict> getDictList(String code);

    /**
     * 根据字典编码查询字典
     * @param code 字典编码
     *             不能为空
     * @return 字典
     */
    Map<String,String> getDictMap(String code);
}
