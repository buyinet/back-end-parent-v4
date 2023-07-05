package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoUserCharm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 用户魅力值表
 * @author 方某方
 */
public interface BusOvoUserCharmRepository extends JpaRepository<BusOvoUserCharm, Long> {

    /**
     * 增加魅力值
     * @param userId 用户id
     * @param value 增加的魅力值
     * @return 增加结果
     */
    @Query(value = "update BusOvoUserCharm set value = value + ?2 where userId = ?1")
    long increaseValue(Long userId, Long value);
}
