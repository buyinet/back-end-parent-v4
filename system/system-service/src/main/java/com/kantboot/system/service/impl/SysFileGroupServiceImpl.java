package com.kantboot.system.service.impl;

import com.kantboot.system.module.entity.SysFileGroup;
import com.kantboot.system.repository.SysFileGroupRepository;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.system.service.ISysFileGroupService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 *     文件组的服务实现类
 *     1. 用于管理文件组
 *     2. 用于管理文件组下的文件
 *     3. 用于管理文件组下的文件的访问权限
 * </p>
 * @author 方某方
 */
@Service
public class SysFileGroupServiceImpl implements ISysFileGroupService {

    @Resource
    private SysFileGroupRepository repository;

    @Resource
    private ISysExceptionService exceptionService;

    @Override
    public SysFileGroup create(String code, String name) {
        SysFileGroup byCode = repository.findByCode(code);
        if (byCode != null) {
            // 提供 文件组编码重复 的异常
            throw exceptionService.getException("duplicateFileGroupCode");
        }
        SysFileGroup sysFileGroup = new SysFileGroup().setCode(code).setName(name);
        return repository.save(sysFileGroup);
    }

}






