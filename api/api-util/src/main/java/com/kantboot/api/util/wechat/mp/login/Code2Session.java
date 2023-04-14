package com.kantboot.api.util.wechat.mp.login;


import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.kantboot.util.common.exception.BaseException;
import lombok.Data;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * code2Session
 * <a href="https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code">...</a>
 * @author 方某方
 */
public class Code2Session {

    /**
     * 请求参数
     * @author 方某方
     */
    @Data
    public static class Param{

        /**
         * 微信小程序appid
         */
        private String appid;

        /**
         * 微信小程序secret
         */
        private String secret;

        /**
         * 微信小程序code
         */
        @JSONField(name = "js_code")
        private String jsCode;

        /**
         * 授权类型
         */
        @JSONField(name = "grant_type")
        private String grantType ="authorization_code";

    }


    @Data
    public static class Result{

        /**
         * 用户唯一标识
         */
        private String openid;

        /**
         * 会话密钥
         */
        @JSONField(name = "session_key")
        private String sessionKey;

        /**
         * 用户在开放平台的唯一标识符
         */
        private String unionid;

        /**
         * 错误码
         */
        private String errcode;

        /**
         * 错误信息
         */
        private String errmsg;

    }


    /**
     * 获取code2Session的请求结果
     * @param param 请求参数
     * @return 请求结果
     */
    @SneakyThrows
    public static Code2Session.Result getResult(Code2Session.Param param){

        OkHttpClient client = new OkHttpClient();

        String url = "https://api.weixin.qq.com/sns/jscode2session"
                + "?appid=" + param.getAppid()
                + "&secret=" + param.getSecret()
                + "&js_code=" + param.getJsCode()
                + "&grant_type=" + param.getGrantType();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        assert response.body() != null;
        String responseBody = response.body().string();
        Code2Session.Result result = JSONObject.parseObject(responseBody, Code2Session.Result.class);
        String errcode = result.getErrcode();
        if (errcode == null) {
            return JSONObject.parseObject(responseBody, Code2Session.Result.class);
        }

        String errcodeOfInvalidCode = "40029";
        if(errcode.equals(errcodeOfInvalidCode)){
            throw new BaseException(40029, "invalidCode", "code无效");
        }
        String errcodeOfInvalidAppid = "40125";
        if(errcode.equals(errcodeOfInvalidAppid)){
            throw new BaseException(40125, "invalidAppid", "appid无效");
        }
        String errcodeOfFrequencyLimit = "45011";
        if(errcode.equals(errcodeOfFrequencyLimit)){
            throw new BaseException(45011, "frequencyLimit", "频率限制");
        }
        String errcodeOfCodeBlocked = "40226";
        if(errcode.equals(errcodeOfCodeBlocked)){
            throw new BaseException(40226, "codeBlocked", "code被拦截");
        }
        String errcodeOfSystemError = "-1";
        if(errcode.equals(errcodeOfSystemError)){
            throw new BaseException(-1, "systemError", "系统错误");
        }
        return JSONObject.parseObject(responseBody, Code2Session.Result.class);

    }

}