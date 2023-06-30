package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * 余额的仓库接口
 * @author 方某方
 */
public interface SysBalanceRepository   extends
        Repository<SysBalance,Long>, JpaRepository<SysBalance,Long> {

    /**
     * 根据用户id查询余额
     * @param userId 用户id
     *               不能为空
     * @return 余额
     */
    List<SysBalance> findByUserId(Long userId);

    SysBalance findByUserIdAndBalanceTypeCode(Long userId,String balanceTypeCode);
}
