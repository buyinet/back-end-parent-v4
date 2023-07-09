package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoUserCharmDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 魅力值明细表
 * @author 方某方
 */
public interface BusOvoUserCharmDetailRepository
extends JpaRepository<BusOvoUserCharmDetail, Long> {

    Page<BusOvoUserCharmDetail> findByToUserIdOrderByGmtCreateDesc(Long toUserId, Pageable page);

    /**
     * 根据fromUserId查询
     * @param fromUserId
     * @param page
     * @return 魅力值明细
     */
    Page<BusOvoUserCharmDetail> findByFromUserIdOrderByGmtCreateDesc(Long fromUserId, Pageable page);
}
