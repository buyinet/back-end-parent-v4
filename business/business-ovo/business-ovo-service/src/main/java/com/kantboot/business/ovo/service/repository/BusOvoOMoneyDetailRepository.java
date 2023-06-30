package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoOMoneyDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * O币订单支付的数据仓库类
 * @author 方某方
 */
public interface BusOvoOMoneyDetailRepository
        extends JpaRepository<BusOvoOMoneyDetail,Long> {

    BusOvoOMoneyDetail findByOrderId(Long orderId);


    /**
     * 根据用户id和时间倒序获取
     * @param userId 用户id
     * @param pageable 分页
     * @return O币订单列表
     */
    Page<BusOvoOMoneyDetail> findByUserIdOrderByGmtCreateDesc(Long userId, Pageable pageable);


}
