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
        baiduTranslateParam.setAppid("20230305001586859");
        baiduTranslateParam.setSecret("9LCopmD6f497X_0HzHIf");
        baiduTranslateParam.setQ("你好");
        baiduTranslateParam.setFrom("zh");
        baiduTranslateParam.setTo("en");
        System.out.println(JSON.toJSONString(BaiduTranslateUtil.translate(baiduTranslateParam)));
    }

}
