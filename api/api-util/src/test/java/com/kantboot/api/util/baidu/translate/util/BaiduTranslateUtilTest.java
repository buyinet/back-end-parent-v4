package com.kantboot.api.util.baidu.translate.util;

import com.alibaba.fastjson2.JSON;
import com.kantboot.api.util.baidu.translate.entity.BaiduTranslateParam;
import org.junit.jupiter.api.Test;

/**
 * 百度翻译工具类的测试类
 * @author 方某方
 */
public class BaiduTranslateUtilTest {

    @Test
    void translate() {
        BaiduTranslateParam baiduTranslateParam = new BaiduTranslateParam();
        baiduTranslateParam.setAppid("百度翻译开放平台的appid");
        baiduTranslateParam.setSecret("百度翻译开放平台的密钥");

        // 翻译的文本
        baiduTranslateParam.setQ("你好");
        // 翻译的源语言，可以使用百度翻译开放平台的语言列表中的编码，后期会扩展也可以使用国际化的语言编码
        baiduTranslateParam.setFrom("zh");
        // 翻译的目标语言，可以使用百度翻译开放平台的语言列表中的编码，后期会扩展也可以使用国际化的语言编码
        baiduTranslateParam.setTo("en");

        // 输出结果：{"src":"你好","dst":"Hello"}
        System.out.println(JSON.toJSONString(BaiduTranslateUtil.translate(baiduTranslateParam)));
    }

}
