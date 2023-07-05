package com.kantboot.business.ovo.service.repository;


import com.kantboot.business.ovo.module.entity.BusOvoGift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 礼物表的数据仓库类
 * 用于操作数据库中的礼物表
 *
 * @author 方某方
 */
public interface BusOvoGiftRepository
        extends JpaRepository<BusOvoGift, Long> {

    /**
     * 根据礼物编码查询礼物
     *
     * @param code 礼物编码
     * @return 礼物
     */
    BusOvoGift findByCode(String code);


    /**
     * 获取所有礼物，并按照优先级倒序
     * @return 所有礼物
     */
    List<BusOvoGift> findAllByOrderByPriorityDesc();


}














