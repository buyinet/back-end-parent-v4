package com.kantboot.system.repository;

import com.kantboot.system.module.entity.SysDictI18n;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * 字典国际化仓库
 * @author 方某方
 */
public interface SysDictI18nRepository extends Repository<SysDictI18n, Long>, JpaRepository<SysDictI18n, Long> {


    /**
     * 根据语言编码和字典编码获取字典国际化
     * @param languageCode 语言编码
     * @param dictGroupCode 字典编码
     * @return 字典国际化
     */
    List<SysDictI18n> findByLanguageCodeAndDictGroupCode(String languageCode, String dictGroupCode);

    /**
     * 根据语言编码和字典编码获取字典国际化
     * @param languageCode 语言编码
     * @param dictGroupCode 字典编码
     * @param dictCode 字典编码
     * @return 字典国际化
     */
    SysDictI18n findByLanguageCodeAndDictGroupCodeAndDictCode(String languageCode, String dictGroupCode, String dictCode);

}
