package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * 文件的Repository
 * @author 方某方
 */
public interface SysFileRepository extends Repository<SysFile, Long>, CrudRepository<SysFile, Long> {

    /**
     * 根据uuid获取文件
     * @param id 文件的id
     * @return 文件
     */
    SysFile findByIdAndGroupCodeIsNull(Long id);

    /**
     * 根据uuid获取文件
     * @param id 文件的id
     * @param groupCode 文件组编码
     * @return 文件
     */
    SysFile findByIdAndGroupCode(Long id, String groupCode);

    /**
     * 根据md5获取文件
     * @param md5 文件的md5
     * @param groupCode 文件组编码
     * @return 文件
     */
    List<SysFile> findByMd5AndGroupCode(String md5, String groupCode);

}
