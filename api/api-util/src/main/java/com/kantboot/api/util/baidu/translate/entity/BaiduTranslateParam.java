package com.kantboot.api.util.baidu.translate.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 百度翻译通用文本的参数
 * @author 方某方
 */
@Data
@Accessors(chain = true)
public class BaiduTranslateParam {

    /**
     * 百度翻译的appid
     */
    private String appid;

    /**
     * 百度翻译的密钥
     */
    private String secret;

    /**
     * 翻译的文本
     */
    private String q;

    /**
     * 翻译的源语言
     */
    private String from;

    /**
     * 翻译的目标语言
     */
    private String to;

}
