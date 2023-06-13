package com.kantboot.file.system.controller;

import com.kantboot.file.service.service.ISysFileService;
import com.kantboot.system.file.SysFile;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理的Controller
 * 用于管理文件的上传、下载、删除等
 * @author
 */
@RestController
@RequestMapping("/system-file/file")
public class SysFileController {

    @Resource
    private ISysFileService sysFileService;

    /**
     * 上传文件
     * @param file 文件
     * @param groupCode 文件组编码
     * @return 文件
     */
    @RequestMapping("/upload")
    public RestResult<SysFile> upload(MultipartFile file, String groupCode) {
        return RestResult.success(sysFileService.upload(file, groupCode), "上传成功");
    }

    /**
     * 访问文件
     */
    @RequestMapping("/visit/{id}")
    public ResponseEntity<FileSystemResource> visit(
            @PathVariable Long id){
        return sysFileService.visit(id);
    }


}
