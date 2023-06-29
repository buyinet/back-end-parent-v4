package com.kantboot.pay.service.repository;

import com.kantboot.pay.module.entity.PayOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 关于支付的仓库
 * @author 方某方
 */
public interface PayRepository extends JpaRepository<PayOrder,Long> {
}
