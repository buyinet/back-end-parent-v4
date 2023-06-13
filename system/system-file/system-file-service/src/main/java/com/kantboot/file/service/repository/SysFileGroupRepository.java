package com.kantboot.file.service.repository;

import com.kantboot.system.file.SysFileGroup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 文件组管理的JPA接口
 * @author 方某方
 */
public interface SysFileGroupRepository extends JpaRepository<SysFileGroup, Long> {

    /**
     * 根据文件组编码获取文件组
     * @param code 文件组编码
     * @return 文件组
     */
    SysFileGroup findByCode(String code);

}
