package com.kantboot.system.service;

import com.kantboot.system.module.entity.SysLanguage;

import java.util.List;

/**
 * 系统语言服务接口
 * @author 方某方
 */
public interface ISysLanguageService {

    /**
     * 返回所有支持的语言
     * @return 结果
     */
    List<SysLanguage> getLanguages();


}
