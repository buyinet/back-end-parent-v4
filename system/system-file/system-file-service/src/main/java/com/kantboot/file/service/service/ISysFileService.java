package com.kantboot.file.service.service;

import com.kantboot.system.file.SysFile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理的Service接口
 * 用于管理文件的上传、下载、删除等
 * @author 方某方
 */
public interface ISysFileService {

    /**
     * 上传文件(需要指定文件组编码)
     * @param file 文件
     * @param groupCode 文件组编码
     * @return 文件
     */
    SysFile upload(MultipartFile file, String groupCode);


    /**
     * 根据id查询文件
     * @param id 文件id
     * @return 文件
     */
    SysFile getById(Long id);

    /**
     * 查看文件
     * @param id 文件id
     * @return 文件
     */
    ResponseEntity<FileSystemResource> visit(Long id);

}
