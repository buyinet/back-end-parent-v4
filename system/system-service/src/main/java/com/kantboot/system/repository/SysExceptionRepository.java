package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * 异常仓库
 * @author 方某方
 */
public interface SysExceptionRepository extends JpaRepository<SysException, Long>, Repository<SysException, Long> {

    /**
     * 根据code查询异常
     * @param code 异常编码
     * @return 异常
     */
    SysException findByCode(String code);

}
