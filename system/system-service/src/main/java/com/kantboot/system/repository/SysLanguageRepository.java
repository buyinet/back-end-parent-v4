package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * 系统语言的仓库接口
 * @author 方某方
 */
public interface SysLanguageRepository extends Repository<SysLanguage,Long>, JpaRepository<SysLanguage,Long> {

    /**
     * 获取支持的语言
     * @return 支持的语言
     */
    List<SysLanguage> findAllBySupportIsTrue();

    /**
     * 根据百度翻译的语言编码获取语言
     * @param baiduTranslateCode 百度翻译的语言编码
     * @param support 是否支持
     * @return 语言
     */
    List<SysLanguage> findByBaiduTranslateCodeAndSupport(String baiduTranslateCode, boolean support);

}
