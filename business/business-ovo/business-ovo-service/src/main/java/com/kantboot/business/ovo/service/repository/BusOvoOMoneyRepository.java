package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoOMoney;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * O币的仓库类
 * @author 方奕丰
 */
public interface BusOvoOMoneyRepository extends JpaRepository<BusOvoOMoney, Long> {

    /**
     * 根据状态编码获取O币
     * @param statusCode 状态编码
     * @return O币列表
     */
    List<BusOvoOMoney> findByStatusCodeOrderByPriorityDesc(String statusCode);

}
