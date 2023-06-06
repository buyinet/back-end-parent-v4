package com.kantboot.system.service;

import java.util.Map;

/**
 * 余额类型的服务接口
 * @author 方某方
 */
public interface ISysBalanceTypeService {

    /**
     * 获取余额类型的map
     * @return map
     */
    Map<String,String> getMap();
}
