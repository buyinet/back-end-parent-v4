package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * 页面仓库接口
 * 用于操作页面表
 * @author 方某方
 */
public interface SysPageRepository extends Repository<SysPage,Long>, JpaRepository<SysPage,Long> {

    /**
     * 根据页面编码查询页面
     * @param code 页面编码
     *             不能为空
     * @return 页面
     */
    SysPage findByCode(String code);

}
