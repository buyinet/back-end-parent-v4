package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.entity.BusOvoOMoney;
import com.kantboot.util.common.result.PageResult;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * O币的服务类
 * @author 方某方
 */
public interface IBusOvoOMoneyService {

    List<BusOvoOMoney> getOvoOMoneyList();

    /**
     * 购买O币
     * @param id O币id
     * @return 购买结果
     */
    Long buy(Long id);

    /**
     * 充值成功的回调
     */
    void rechargeCallback(String orderId);



}
