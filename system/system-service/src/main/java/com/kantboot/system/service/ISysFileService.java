package com.kantboot.system.service;

import com.kantboot.system.module.entity.SysFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务接口
 * @author 方某方
 */
public interface ISysFileService {

    /**
     * 获取文件信息
     * @param file 文件
     * @return 文件信息
     */
    SysFile getFileInfo(MultipartFile file);


    /**
     * 上传文件
     * @param file 文件
     * @return 文件
     */
    SysFile uploadFile(MultipartFile file);

    /**
     * 上传文件(需要指定文件组编码)
     * @param file 文件
     * @param groupCode 文件组编码
     * @return 文件
     */
    SysFile uploadFile(MultipartFile file,String groupCode);


    /**
     * 访问文件
     * @param id 文件UUID
     * @return 文件
     */
    void visitFile(Long id);

    /**
     * 根据uuid和文件组编码获取文件
     * @param id 文件id
     *             1. 不能为null
     *             2. 不能为""
     * @param groupCode 文件组编码
     * @return 文件
     */
    void visitFile(String groupCode,Long id);



    /**
     * 上传网络文件
     * @param url 网络文件地址
     * @return 文件
     */
    SysFile uploadNetFile(String url);



}
