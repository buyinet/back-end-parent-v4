package com.kantboot.system.service;

import java.util.Map;

/**
 * 余额服务接口
 * @author 方某方
 */
public interface ISysBalanceService {

    /**
     * 获取自身的余额
     * @return 余额
     */
    Map<String,Double> getSelfMap();


}
