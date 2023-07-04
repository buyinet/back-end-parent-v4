package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoUserGiftDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 礼物赠送明细表的数据仓库类
 * 用于操作数据库中的礼物赠送明细表
 * @author 方某方
 */
public interface BusOvoUserGiftDetailRepository
extends JpaRepository<BusOvoUserGiftDetail, Long>
{
}
