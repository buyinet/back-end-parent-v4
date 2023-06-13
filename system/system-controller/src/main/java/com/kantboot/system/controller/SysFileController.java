package com.kantboot.system.controller;

import com.kantboot.system.module.entity.SysFile;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.system.service.ISysDictI18nService;
import com.kantboot.system.service.ISysFileService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务前端控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/system/file")
public class SysFileController {

    @Resource
    private ISysFileService service;

    @Resource
    private ISysDictI18nService dictI18nService;

    @Resource
    private IStateSuccessService stateSuccessService;


    @RequestMapping("/getFileInfo")
    public RestResult getFileInfo(MultipartFile file){
        return  stateSuccessService.success(service.getFileInfo(file), "getSuccess");
    }

    /**
     * 上传文件
     * @param file 文件
     * @return 文件
     */
    @RequestMapping("/uploadFile")
    public RestResult<SysFile> uploadFile(MultipartFile file,
                                          @DefaultValue("") String groupCode){

        // 如果文件组编码为空
        if("".equals(groupCode)||groupCode==null){
            // 上传文件
            SysFile sysFile = service.uploadFile(file);
            // 上传成功
            return  stateSuccessService.success(sysFile,"uploadSuccess");
        }

        // 上传文件
        SysFile sysFile = service.uploadFile(file,groupCode);
        // 上传成功
        return stateSuccessService.success(sysFile,"uploadSuccess");
    }
    
    /**
     * 上传网络文件
     * @param url 网络文件地址
     * @return 文件
     */
    @RequestMapping("/uploadNetFile")
    public RestResult<SysFile> uploadNetFile(String url){
        SysFile sysFile = service.uploadNetFile(url);
        // 上传成功
        return stateSuccessService.success(sysFile,"uploadSuccess");
    }

    /**
     * 访问文件
     * @param id 文件ID
     * @return 文件
     */
    @RequestMapping("/visitFile/{id}")
    public ResponseEntity<FileSystemResource> visitFile(@PathVariable("id") Long id){
        return service.visitFile(id);
    }

    /**
     * 访问文件（存在文件组编码）
     * @param id 文件ID
     * @param groupCode 文件组编码
     * @return 文件
     */
    @RequestMapping("/visitFile/{groupCode}/{id}")
    public ResponseEntity<FileSystemResource>  visitFile(
            @PathVariable("groupCode") String groupCode,
            @PathVariable("id") Long id
    ){
        return service.visitFile(groupCode,id);
    }

}
