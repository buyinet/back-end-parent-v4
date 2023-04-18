package com.kantboot.system.repository;

import com.kantboot.system.module.entity.RelSysPageAndSysDict;
import com.kantboot.system.module.entity.SysPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * 页面和字典关联仓库接口
 * 用于操作页面和字典关联表
 * @author 方某方
 */
public interface RelSysPageAndSysDictRepository extends
        Repository<RelSysPageAndSysDict,Long>, JpaRepository<RelSysPageAndSysDict,Long> {

    /**
     * 根据页面编码查询页面
     * @param pageCode 页面编码
     *                 不能为空
     * @return 页面
     */
    List<RelSysPageAndSysDict> findByPageCode(String pageCode);

}
