package com.kantboot.api.service;

import com.kantboot.api.util.baidu.translate.entity.BaiduTranslateResult;

/**
 * 百度翻译服务接口
 *
 * @author 方某方
 */
public interface IBaiduTranslateService {

    /**
     * 翻译
     *
     * @param q    翻译的文本
     * @param from 翻译的源语言
     * @param to   翻译的目标语言
     * @return 翻译结果
     */
    BaiduTranslateResult translate(String q, String from, String to);

    /**
     * 通过百度翻译生成国际化字典
     * @param q 待翻译的文本
     * @param from 源语言
     * @param dictGroupCode 字典分组编码
     * @param dictCode 字典子级编码
     */
    void generateDictI18n(String q, String from,String dictGroupCode,String dictCode);

}