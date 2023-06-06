package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysBalanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * 余额类型的仓库接口
 * @author 方某方
 */
public interface SysBalanceTypeRepository  extends
        Repository<SysBalanceType,Long>, JpaRepository<SysBalanceType,Long> {
}
