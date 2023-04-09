package com.kantboot.api.util.baidu.translate.util;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.api.util.baidu.translate.entity.BaiduTranslateParam;
import com.kantboot.api.util.baidu.translate.entity.BaiduTranslateResult;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 百度翻译工具类
 * @author 方某方
 */
@Log4j2
public class BaiduTranslateUtil {

    /**
     * 百度通用翻译
     * @param param 百度翻译通用文本的实体类
     * @return 翻译后的文本
     */
    @SneakyThrows
    public static BaiduTranslateResult translate(BaiduTranslateParam param) {
        String uri = "https://fanyi-api.baidu.com/api/trans/vip/translate";

        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        String appid= param.getAppid();

        // 密钥
        String key = param.getSecret();

        // 签名
        String sign = MD5.create().digestHex(appid + param.getQ() + salt + key, "UTF-8");

        // 参数,要url转换
        String urlParam="?q="+ URLEncoder.encode(param.getQ(), StandardCharsets.UTF_8) +
                "&from="+param.getFrom()
                +"&to="+param.getTo()
                +"&appid="
                +appid
                +"&salt="+salt
                +"&sign="+sign;

        String url = uri + urlParam;

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();

        try (okhttp3.Response response = new okhttp3.OkHttpClient().newCall(request).execute()) {
            assert response.body() != null;
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            JSONObject transResult = jsonObject.getJSONArray("trans_result").getJSONObject(0);
            BaiduTranslateResult baiduTranslateResult = new BaiduTranslateResult();
            baiduTranslateResult.setSrc(transResult.getString("src"));
            baiduTranslateResult.setDst(transResult.getString("dst"));
            return baiduTranslateResult;
        }catch (Exception e){
            log.error("百度翻译异常", e);
        }

        return null;
    }
}
