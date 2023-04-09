package com.kantboot.api.util.baidu.translate.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 百度翻译的结果实体类
 * @author 方某方
 */
@Data
public class BaiduTranslateResult {

    /**
     * 翻译的源文本
     */
    private String src;

    /**
     * 翻译的目标文本
     */
    private String dst;
}
