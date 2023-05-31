package com.kantboot.system.repository;


import com.kantboot.system.module.entity.SysFileGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * 文件组的仓库接口
 * @author 方某方
 */
public interface SysFileGroupRepository extends Repository<SysFileGroup, Long>, JpaRepository<SysFileGroup, Long> {

    /**
     * 根据code获取文件组
     * @param code 文件组编码
     * @return 文件组
     */
    SysFileGroup findByCode(String code);

}
