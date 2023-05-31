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
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

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
     * 读取文件
     *
     * @param response 响应
     * @param filePath 文件路径
     */
    private void readFile(HttpServletResponse response, SysFile sysFile,String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("File not found");
        }
        try {
            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();

            // 设置响应头
            response.setHeader("Content-Disposition", "attachment;filename=" + sysFile.getMd5()+ "." + sysFile.getType());
            response.setHeader("Content-Type", sysFile.getContentType());
            response.setHeader("Content-Length", String.valueOf(sysFile.getSize()));
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Connection", "Keep-Alive");
            response.setHeader("Cache-Control", "max-age=2592000");

            // 设置响应状态码
            response.setStatus(HttpServletResponse.SC_OK);

            // 写入响应流
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    /**
     * 访问文件
     *
     * @param id 文件ID
     */
    @Override
    public void visitFile(Long id) {
        String path = settingService.getValue("file","uploadPath");
        // 获取文件
        SysFile file = repository.findByIdAndGroupCodeIsNull(id);

        readFile(response, file,path+"/"+file.getPath());

    }

    @Override
    public void visitFile(String groupCode, Long id) {
        SysFileGroup byCode = groupRepository.findByCode(groupCode);
        // 获取文件
        SysFile file = repository.findByIdAndGroupCode(id, groupCode);

        // 设置响应头
        System.out.println(byCode.getPath()+"/"+file.getPath());
        readFile(response,file,byCode.getPath()+"/"+file.getPath());
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
