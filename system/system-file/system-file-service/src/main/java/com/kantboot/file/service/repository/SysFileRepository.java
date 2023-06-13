package com.kantboot.file.service.repository;

import com.kantboot.system.file.SysFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 文件管理的JPA接口
 * @author 方某方
 */
public interface SysFileRepository extends JpaRepository<SysFile, Long> {

    /**
     * 根据md5和文件组编码获取文件
     * @param md5 文件md5
     * @param groupCode 文件组编码
     * @return 文件
     */
    List<SysFile> findByMd5AndGroupCode(String md5, String groupCode);
}
