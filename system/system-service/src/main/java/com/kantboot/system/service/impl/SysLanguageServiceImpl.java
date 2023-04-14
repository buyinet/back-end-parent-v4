package com.kantboot.system.service.impl;

import com.kantboot.system.module.entity.SysLanguage;
import com.kantboot.system.repository.SysLanguageRepository;
import com.kantboot.system.service.ISysLanguageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统语言服务实现类
 * @author 方某方
 */
@Service
public class SysLanguageServiceImpl implements ISysLanguageService {

    @Resource
    private SysLanguageRepository repository;

    /**
     * 返回所有支持的语言
     * @return 结果
     */
    @Override
    public List<SysLanguage> getLanguages() {
        return repository.findAllBySupportIsTrue();
    }

}
