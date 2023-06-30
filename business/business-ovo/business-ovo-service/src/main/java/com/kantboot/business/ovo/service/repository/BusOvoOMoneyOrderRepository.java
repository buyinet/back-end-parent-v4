package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoOMoneyOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * O币订单的数据仓库类
 * @author 方某方
 */
public interface BusOvoOMoneyOrderRepository extends JpaRepository<BusOvoOMoneyOrder,Long> {

    /**
     * 根据订单id查询订单
     * @param orderId 订单id
     * @return 订单
     */
    BusOvoOMoneyOrder findByOrderId(Long orderId);


}
