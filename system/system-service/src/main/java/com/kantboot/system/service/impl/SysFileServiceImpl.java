package com.kantboot.system.service.impl;


import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.system.module.entity.SysFile;
import com.kantboot.system.module.entity.SysFileGroup;
import com.kantboot.system.module.entity.SysSetting;
import com.kantboot.system.repository.SysFileGroupRepository;
import com.kantboot.system.repository.SysFileRepository;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.system.service.ISysFileService;
import com.kantboot.system.service.ISysSettingService;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.util.common.file.FileUtil;
import com.kantboot.util.common.http.HttpRequestHeaderUtil;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 文件服务实现类
 *
 * @author 方某方
 */
@Slf4j
@Service
public class SysFileServiceImpl implements ISysFileService {

    @Resource
    private SysFileRepository repository;

    @Resource
    private ISysSettingService settingService;

    @Resource
    private ISysUserService userService;

    @Resource
    private HttpRequestHeaderUtil headerUtil;

    @Resource
    private HttpServletResponse response;

    @Resource
    private SysFileGroupRepository groupRepository;

    @Resource
    private ISysExceptionService exceptionService;

    @Resource
    private RedisUtil redisUtil;

    @SneakyThrows
    @Override
    public SysFile getFileInfo(MultipartFile file) {

        // 获取文件MD5
        String md5 = DigestUtils.md5DigestAsHex(file.getBytes());

        // 获取文件信息
        Long size = file.getSize();

        // 获取文件类型
        String contentType = FileUtil.getContentType(file.getOriginalFilename());

        return new SysFile()
                .setMd5(md5)
                .setSize(size)
                .setType(contentType);
    }

    /**
     * 上传文件
     *
     * @param file           文件
     * @param fileUploadPath 文件上传路径
     * @return 文件信息
     */
    @SneakyThrows
    private SysFile frontUploadFile(MultipartFile file, String fileUploadPath, String groupCode) {
        // 生成文件UUID
        String uuid = IdUtil.simpleUUID();

        // 获取文件MD5
        String md5 = DigestUtils.md5DigestAsHex(file.getBytes());
        // 检查文件是否已存在
        List<SysFile> byMd5 = repository.findByMd5AndGroupCode(md5, groupCode);

        // 如果文件已存在
        if (byMd5.size() > 0) {
            // 返回已存在的文件信息
            SysFile save = repository.save(new SysFile()
                    .setMd5(md5)
                    .setGroupCode(groupCode)
                    .setPath(byMd5.get(0).getPath())
                    .setName(byMd5.get(0).getName())
                    .setSize(byMd5.get(0).getSize())
                    .setType(byMd5.get(0).getType())
                            .setContentType(byMd5.get(0).getContentType())
                    .setUserIdOfUpload(userService.getSelf().getId())
                    .setIpOfUpload(headerUtil.getIp())

            );
            return save.setPath(null);
        }


        File fileUploadPathM = new File(fileUploadPath);
        // 如果文件夹不存在则创建
        if (!fileUploadPathM.exists()) {
            boolean mkdirs = fileUploadPathM.mkdirs();
            if (!mkdirs) {
                throw new Exception("文件夹创建失败");
            }
        }

        String fileName = file.getOriginalFilename();
        log.info("文件名：{}", fileName);
        // assert关键字用于断言，如果表达式为false，则抛出AssertionError
        assert fileName != null;
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
        String path = uuid +"."+ suffix;
        String contentType = FileUtil.getContentType(suffix);

        try {
            file.transferTo(new File(fileUploadPath+"/"+path));
        } catch (Exception e) {
            e.printStackTrace();
        }

        SysFile save = repository.save(new SysFile()
                .setGroupCode(groupCode)
                .setMd5(md5)
                .setPath(path)
                .setName(fileName)
                .setSize(file.getSize())
                .setType(suffix)
                .setContentType(contentType)
                .setUserIdOfUpload(userService.getSelf().getId())
                .setIpOfUpload(headerUtil.getIp())
        );
        repository.save(save);
        return save.setPath(null);
    }

    @SneakyThrows
    @Override
    public SysFile uploadFile(MultipartFile file) {
        // 获取文件上传路径
        String path = settingService.getValue("file","uploadPath");
        String fileUploadPath = path + "/";
        return frontUploadFile(file, fileUploadPath, null);
    }

    @SneakyThrows
    @Override
    public SysFile uploadFile(MultipartFile file, String groupCode) {
        SysFileGroup byCode = groupRepository.findByCode(groupCode);
        if (byCode == null) {
            // 如果文件组不存在则抛出异常，提示文件组不存在
            throw exceptionService.getException("groupCodeNotExist");
        }
        // 获取文件上传路径
        String fileUploadPath = byCode.getPath() + "/";
        return frontUploadFile(file, fileUploadPath, groupCode);
    }

    /**
     * 通过文件id得到文件路径
     * @param id 文件id
     * @return 文件路径
     */
    private String getPathById(Long id) {
        String redisKey = "file:"+id+":settingPath";
        String path = redisUtil.get(redisKey);
        if (path != null) {
            return path;
        }
        String settingPath  = settingService.getValue("file","uploadPath");
        // 获取文件
        SysFile sysFile = repository.findByIdAndGroupCodeIsNull(id);
        redisUtil.setEx(redisKey,settingPath+"/"+sysFile.getPath(),7, TimeUnit.DAYS);
        return settingPath+"/"+sysFile.getPath();
    }

    /**
     * 访问文件
     *
     * @param id 文件ID
     */
    @Override
    public ResponseEntity<FileSystemResource> visitFile(Long id) {
        String path = settingService.getValue("file","uploadPath");
        // 获取文件
        SysFile sysFile = repository.findByIdAndGroupCodeIsNull(id);
        File file = new File(path+"/"+sysFile.getPath());
        FileSystemResource resource = new FileSystemResource(file);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", file.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    /**
     * 通过文件组和文件id得到文件路径
     * @param groupCode 文件组编码
     * @param id 文件id
     * @return 文件路径
     */
    private String getPathByGroupCodeAndId(String groupCode, Long id) {
        String redisKey="fileId:"+id+ ":path";
        String s = redisUtil.get(redisKey);
        if (s!=null){
            return s;
        }
        SysFileGroup byCode = groupRepository.findByCode(groupCode);
        SysFile sysFile = repository.findByIdAndGroupCode(id, groupCode);
        redisUtil.setEx(redisKey,byCode.getPath()+"/"+sysFile.getPath(),30, TimeUnit.DAYS);
        return byCode.getPath()+"/"+sysFile.getPath();
    }


    @Override
    public ResponseEntity<FileSystemResource> visitFile(String groupCode, Long id) {
        File file = new File(getPathByGroupCodeAndId(groupCode, id));
        FileSystemResource resource = new FileSystemResource(file);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", file.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    /**
     * 上传网络文件
     *
     * @return 文件
     */
    @SneakyThrows
    @Override
    public SysFile uploadNetFile(String url) {

        // 根据URl下载这个文件
        String fileUploadPath = settingService.getValue("file","uploadPath");
        File fileUploadPathM = new File(fileUploadPath);
        // 如果文件夹不存在则创建
        if (!fileUploadPathM.exists()) {
            boolean mkdirs = fileUploadPathM.mkdirs();
            if (!mkdirs) {
                throw new Exception("文件夹创建失败");
            }
        }
        String fileName = FileUtil.netFileDownload(url, fileUploadPath);
        log.info("文件保存成功[{}]", fileUploadPath + "/" + fileName);
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        log.info("文件后缀[{}]", suffix);
        byte[] bytes = FileUtil.createFileItem(fileUploadPath, fileName).get();
        // 获取文件MD5
        String md5 = DigestUtils.md5DigestAsHex(bytes);
        log.info("文件MD5[{}]", md5);
        // 生成文件UUID
        String uuid = IdUtil.simpleUUID();
        log.info("文件UUID[{}]", uuid);

        // 文件大小
        long fileSize = bytes.length;
        log.info("文件大小[{}]", fileSize);

        Long userIdOfUpload = userService.getSelf().getId();
        log.info("文件上传者ID[{}]", userIdOfUpload);

        String ipOfUpload = headerUtil.getIp();
        log.info("文件上传者IP[{}]", ipOfUpload);

        // 为什么这下面的代码不执行呢？
        SysFile entity = new SysFile()
                .setMd5(md5)
                .setPath(fileUploadPath + "/" + fileName)
                .setName(fileName)
                .setSize(fileSize)
                .setType(suffix)
                .setUserIdOfUpload(userIdOfUpload)
                .setIpOfUpload(ipOfUpload);
        log.info("正在保存，文件信息[{}]", JSONObject.toJSONString(entity));

        SysFile save = repository.save(entity);
        log.info("保存成功，文件信息[{}]", JSONObject.toJSONString(save));

        return save.setPath(null).setId(null);
    }


}
