package com.kantboot.business.ovo.service.service;

import com.kantboot.util.common.result.PageResult;

/**
 * O币明细的服务类
 * @author 方某方
 */
public interface IBusOvoOMoneyDetailService {

    /**
     * 获取O币明细
     * @param userId 用户id
     * @return O币明细
     */
    PageResult get(Long userId, Integer pageNumber);

    /**
     * 获取自己的O币明细
     * @return O币明细
     */
    PageResult getSelf(Integer pageNumber);

}
