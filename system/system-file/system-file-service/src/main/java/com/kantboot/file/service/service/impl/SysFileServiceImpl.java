package com.kantboot.file.service.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.file.service.repository.SysFileRepository;
import com.kantboot.file.service.service.ISysFileGroupService;
import com.kantboot.file.service.service.ISysFileService;
import com.kantboot.system.file.SysFile;
import com.kantboot.system.file.SysFileGroup;
import com.kantboot.util.common.file.FileUtil;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 文件管理的Service接口实现类
 * 用于管理文件的上传、下载、删除等
 * @author 方某某
 */
@Slf4j
@Service
public class SysFileServiceImpl implements ISysFileService {

    @Resource
    private SysFileRepository repository;

    @Resource
    private ISysFileGroupService sysFileGroupService;

    @Resource
    private RedisUtil redisUtil;


    /**
     * 添加进redis中
     */
    private void addRedis(SysFile sysFile) {
        String redisKey = "fileId:" + sysFile.getId() + ":SysFile";
        redisUtil.setEx(redisKey, JSON.toJSONString(sysFile), 7, TimeUnit.DAYS);
    }

    /**
     * 从redis中获取
     * @param id 文件ID
     * @return 文件信息
     */
    private SysFile getByRedis(Long id) {
        String redisKey = "fileId:" + id + ":SysFile";
        String s = redisUtil.get(redisKey);
        if (s != null) {
            return JSONObject.parseObject(s, SysFile.class);
        }
        return null;
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
        // 获取上传时文件名
        String originalName = file.getOriginalFilename();

        // 检查文件是否已存在
        List<SysFile> byMd5 = repository.findByMd5AndGroupCode(md5, groupCode);

        // 如果文件已存在
        if (byMd5.size() > 0) {
            // 返回已存在的文件信息
            SysFile save = repository.save(new SysFile()
                    .setMd5(md5)
                    .setGroupCode(groupCode)
                    .setOriginalName(originalName)
                    .setName(byMd5.get(0).getName())
                    .setSize(byMd5.get(0).getSize())
                    .setContentType(byMd5.get(0).getContentType())
                    .setType(byMd5.get(0).getType())
                    .setContentType(byMd5.get(0).getContentType())
            );
            addRedis(save);
            return save;
        }


        File fileUploadPathM = new File(fileUploadPath);
        // 如果文件夹不存在则创建
        if (!fileUploadPathM.exists()) {
            boolean mkdirs = fileUploadPathM.mkdirs();
            if (!mkdirs) {
                throw new Exception("文件夹创建失败");
            }
        }

        log.info("文件上传：{}", originalName);
        // assert关键字用于断言，如果表达式为false，则抛出AssertionError
        assert originalName != null;
        String suffix = originalName.substring(originalName.lastIndexOf(".")+1);
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
                .setOriginalName(originalName)
                .setName(path)
                .setSize(file.getSize())
                .setContentType(FileUtil.getContentType(path))
                .setType(suffix)
                .setContentType(contentType)
        );
        addRedis(save);
        return save;
    }

    @Override
    public SysFile upload(MultipartFile file, String groupCode) {
        SysFileGroup byCode = sysFileGroupService.getByCode(groupCode);
        if (byCode == null) {
            // 如果文件组不存在则抛出异常，提示文件组不存在
//            throw exceptionService.getException("groupCodeNotExist");
            throw new RuntimeException("文件组不存在");
        }
        // 获取文件上传路径
        String fileUploadPath = byCode.getPath() + "/";
        return frontUploadFile(file, fileUploadPath, groupCode);
    }


    @Override
    public String getNameById(Long id) {
        SysFile redis = getByRedis(id);
        if(redis!=null){
            return redis.getName();
        }

        SysFile sysFile = repository.findById(id).get();
        if(sysFile!=null){
            String name = sysFile.getName();
            addRedis(sysFile);
            return name;
        }
        return null;


    }

    @Override
    public SysFile getById(Long id) {
        SysFile redis = getByRedis(id);
        if(redis!=null){
            return redis;
        }
        SysFile sysFile = repository.findById(id).get();
        addRedis(sysFile);
        return sysFile;
    }

    @Override
    public ResponseEntity<FileSystemResource> visit(Long id) {
        SysFile byId = getById(id);
        String path=byId.getGroup().getPath()+"/"+byId.getName();
        FileSystemResource resource = new FileSystemResource(path);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", CacheControl.maxAge(7, TimeUnit.HOURS).getHeaderValue());
        headers.add("Content-Disposition", "inline;filename=" + byId.getName());
        headers.add("Content-Type", byId.getContentType());
        headers.add("Content-Length", String.valueOf(byId.getSize()));


        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
