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

    /**
     * 根据用户id获取余额
     * @param userId 用户id
     * @return 余额
     */
    Map<String,Double> getByUserId(Long userId);


    /**
     * 对余额进行增加
     * @param typeCode 类型编码
     * @param num 数量
     * @param userId 用户id
     */
    void addBalance(String typeCode,Double num,Long userId);


}
