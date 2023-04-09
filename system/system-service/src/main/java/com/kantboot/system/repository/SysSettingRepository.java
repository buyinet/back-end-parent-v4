package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * 系统设置仓库
 * @author 方某方
 */
public interface SysSettingRepository extends JpaRepository<SysSetting, Long>, Repository<SysSetting, Long> {

    /**
     * 根据分组编码获取系统设置
     * @param groupCode 分组编码
     * @return 系统设置
     */
    List<SysSetting> findByGroupCode(String groupCode);

    /**
     * 根据分组编码和设置编码获取系统设置
     * @param groupCode 分组编码
     * @param code 设置编码
     * @return 系统设置
     */
    SysSetting findByGroupCodeAndCode(String groupCode, String code);
}
